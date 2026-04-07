package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.dto.RefundDto;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.auction.prediction.event.AuctionPriceReferenceSyncRequestedEvent;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.service.BidQueryService;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.item.mapper.AuctionItemMapper;
import com.biddingmate.biddinggo.item.model.AuctionItem;
import com.biddingmate.biddinggo.item.model.AuctionItemStatus;
import com.biddingmate.biddinggo.point.service.PointService;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryResponse;
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WinnerDealServiceImpl implements WinnerDealService {
    private final AuctionMapper auctionMapper;
    private final BidMapper bidMapper;
    private final BidQueryService bidQueryService;
    private final WinnerDealMapper winnerDealMapper;
    private final WinnerDealQueryService winnerDealQueryService;
    private final AuctionItemMapper auctionItemMapper;
    private final PointService pointService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void processClosing(Auction auction) {
        // 최고 입찰자 1명 조회
        List<BidResponse> topBids = bidMapper.getBidsByAuctionId(
                new RowBounds(0, 1),
                auction.getId(),
                "DESC"
        );

        if (topBids != null && !topBids.isEmpty()) {
            // 낙찰
            BidResponse winnerBid = topBids.get(0);
            AuctionItem auctionItem = auctionItemMapper.findById(auction.getItemId());

            // 낙찰가 결정: 비크리 가격이 있으면 사용, 없으면 경매 시작가 사용
            Long finalPrice = (auction.getVickreyPrice() != null)
                    ? auction.getVickreyPrice()
                    : auction.getStartPrice();

            WinnerDeal winnerDeal = WinnerDeal.builder()
                    .auctionId(auction.getId())
                    .winnerId(winnerBid.getBidderId())
                    .sellerId(auction.getSellerId())
                    .winnerPrice(finalPrice)
                    .status("PAID")
                    .deliveryStatus("SHIPPED")
                    .createdAt(LocalDateTime.now())
                    .build();

            winnerDealMapper.insert(winnerDeal);

            // Auction 테이블 상태 및 결과 업데이트
            auction.setStatus(AuctionStatus.ENDED);
            auction.setWinnerId(winnerBid.getBidderId());
            auction.setWinnerPrice(finalPrice);

            int updatedRows = auctionMapper.updateAuctionResult(auction);
            if (updatedRows != 1) {
                throw new CustomException(ErrorType.ITEM_NOT_AUCTIONABLE);
            }

            auctionItemMapper.updateStatus(
                    auction.getItemId(),
                    AuctionItemStatus.SOLD,
                    AuctionItemStatus.ON_AUCTION,
                    null
            );

            publishAuctionPriceReferenceSyncRequestedEvent(auction, auctionItem, finalPrice);

            log.info("낙찰 처리 성공 - Auction ID: {}, Winner: {}, Price: {}",
                    auction.getId(), winnerBid.getBidderId(), finalPrice);

            List<RefundDto> refunds =
                    bidQueryService.findRefundTargetsExcludingWinner(auction.getId(), winnerBid.getBidderId());

            for (RefundDto refund : refunds) {
                pointService.refundBid(refund.getBidderId(), refund.getAmount());
            }

        } else {
            // 유찰
            auction.setStatus(AuctionStatus.ENDED);

            int updatedRows = auctionMapper.updateAuctionResult(auction);
            if (updatedRows != 1) {
                throw new CustomException(ErrorType.ITEM_NOT_AUCTIONABLE);
            }

            auctionItemMapper.updateStatus(
                    auction.getItemId(),
                    AuctionItemStatus.UNSOLD,
                    AuctionItemStatus.ON_AUCTION,
                    null
            );
            log.info("유찰 처리 완료 - Auction ID: {}", auction.getId());
        }
    }


    @Override
    @Transactional
    public void handleMemberDeactivationAfterWinning(Long memberId) {
        log.info("낙찰 후 회원 비활성화 처리 시작 - Member ID: {}", memberId);

        List<WinnerDeal> winnerDeals = winnerDealQueryService.findByMemberId(memberId);

        for (WinnerDeal winnerDeal : winnerDeals) {
            if (isShippingInfoRegistered(winnerDeal)) {
                log.info("낙찰 후 비활성화 거래 유지 - WinnerDeal ID: {}, Auction ID: {}",
                        winnerDeal.getId(), winnerDeal.getAuctionId());
                return;
            }
            // 비활성화 시 낙찰 취소 및 낙찰자의 예치금 환불
            refundAndCancelWinnerDeal(winnerDeal);
        }

        log.info("낙찰 후 비활성화 대상 거래 수 - Member ID: {}, Count: {}", memberId, winnerDeals.size());
    }

    private boolean isShippingInfoRegistered(WinnerDeal winnerDeal) {
        // null, 빈 문자열, 공백만 있는 값은 미입력으로 본다.
        return StringUtils.hasText(winnerDeal.getRecipient())
                && StringUtils.hasText(winnerDeal.getTel())
                && StringUtils.hasText(winnerDeal.getZipcode())
                && StringUtils.hasText(winnerDeal.getAddress())
                && StringUtils.hasText(winnerDeal.getDetailAddress());
    }

    private void refundAndCancelWinnerDeal(WinnerDeal winnerDeal) {
        if ("CANCELLED".equals(winnerDeal.getStatus())) {
            throw new CustomException(ErrorType.WINNER_DEAL_ALREADY_CANCELLED);
        }

        int updatedRows = winnerDealMapper.updateStatus(winnerDeal.getId(), "CANCELLED");
        if (updatedRows != 1) {
            throw new CustomException(ErrorType.WINNER_DEAL_UPDATE_FAILED);
        }

        pointService.refundBid(winnerDeal.getWinnerId(), winnerDeal.getWinnerPrice());
    }

    private void publishAuctionPriceReferenceSyncRequestedEvent(Auction auction, AuctionItem auctionItem, Long winnerPrice) {
        if (auctionItem == null) {
            log.warn("Skip auction price reference sync because auction item is missing. auctionId={}, itemId={}", auction.getId(), auction.getItemId());
            return;
        }

        eventPublisher.publishEvent(AuctionPriceReferenceSyncRequestedEvent.builder()
                .auctionId(auction.getId())
                .itemId(auctionItem.getId())
                .categoryId(auctionItem.getCategoryId())
                .brand(auctionItem.getBrand())
                .name(auctionItem.getName())
                .quality(auctionItem.getQuality())
                .description(auctionItem.getDescription())
                .winnerPrice(winnerPrice)
                .completedAt(LocalDateTime.now())
                .build());
    }
}
