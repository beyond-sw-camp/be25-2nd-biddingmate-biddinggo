package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.AuctionItemMapper;
import com.biddingmate.biddinggo.item.model.AuctionItemStatus;
import com.biddingmate.biddinggo.point.service.PointService;
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
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
    private final WinnerDealMapper winnerDealMapper;
    private final WinnerDealQueryService winnerDealQueryService;
    private final AuctionItemMapper auctionItemMapper;
    private final PointService pointService;

    @Override
    @Transactional
    public void processClosing(Auction auction) {
        List<BidResponse> topBids = bidMapper.getBidsByAuctionId(
                new RowBounds(0, 1),
                auction.getId(),
                "DESC"
        );

        if (topBids != null && !topBids.isEmpty()) {
            BidResponse winnerBid = topBids.get(0);

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

            log.info("낙찰 처리 성공 - Auction ID: {}, Winner: {}, Price: {}",
                    auction.getId(), winnerBid.getBidderId(), finalPrice);
            return;
        }

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
}
