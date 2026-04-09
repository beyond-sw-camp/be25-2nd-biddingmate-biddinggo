package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.dto.RefundDto;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.auction.prediction.event.AuctionPriceReferenceSyncRequestedEvent;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.service.BidQueryService;
import com.biddingmate.biddinggo.bid.service.BidService;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.AuctionItemMapper;
import com.biddingmate.biddinggo.item.model.AuctionItem;
import com.biddingmate.biddinggo.item.model.AuctionItemStatus;
import com.biddingmate.biddinggo.notification.model.NotificationType;
import com.biddingmate.biddinggo.notification.service.NotificationPublisher;
import com.biddingmate.biddinggo.point.service.PointService;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealShippingAddressRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealTrackingNumberRequest;
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDealStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WinnerDealServiceImpl implements WinnerDealService {
    private static final DateTimeFormatter DEAL_NUMBER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int DEAL_NUMBER_MAX_RETRY_COUNT = 10;

    private final AuctionMapper auctionMapper;
    private final BidMapper bidMapper;
    private final BidService bidService;
    private final BidQueryService bidQueryService;
    private final WinnerDealMapper winnerDealMapper;
    private final WinnerDealQueryService winnerDealQueryService;
    private final AuctionItemMapper auctionItemMapper;
    private final PointService pointService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationPublisher notificationPublisher;

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
                    .dealNumber(generateDealNumber())
                    .winnerPrice(finalPrice)
                    .status(WinnerDealStatus.PAID)
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

            // 낙찰 알람
            notificationPublisher.publishNotification(
                    winnerBid.getBidderId(),
                    NotificationType.WIN,
                    "축하합니다. 경매 #" + auction.getId() + " 낙찰이 확정되었습니다.",
                    "/auctions/" + auction.getId()
            );


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

            // 유찰 알람
            notificationPublisher.publishNotification(
                    auction.getSellerId(),
                    NotificationType.AUCTION_UNSOLD,
                    "경매 #" + auction.getId() + "가 유찰되었습니다.",
                    "/auctions/" + auction.getId()
            );

        }
    }

    @Override
    @Transactional
    public void handleMemberDeactivationAfterWinning(Long memberId) {
        log.info("낙찰 회원 비활성화 처리 시작 - Member ID: {}", memberId);

        List<WinnerDeal> winnerDeals = winnerDealQueryService.findByMemberId(memberId);

        for (WinnerDeal winnerDeal : winnerDeals) {
            if (isShippingInfoRegistered(winnerDeal)) {
                log.info("낙찰 회원 비활성화 거래 유지 - WinnerDeal ID: {}, Auction ID: {}",
                        winnerDeal.getId(), winnerDeal.getAuctionId());
                return;
            }
            refundAndCancelWinnerDeal(winnerDeal);
        }

        log.info("낙찰 회원 비활성화 대상 거래 수 - Member ID: {}, Count: {}", memberId, winnerDeals.size());
    }

    @Override
    @Transactional
    public void registerShippingAddress(Long winnerDealId, Long memberId, WinnerDealShippingAddressRequest request) {
        WinnerDeal winnerDeal = winnerDealMapper.findById(winnerDealId);

        if (winnerDeal == null) {
            throw new CustomException(ErrorType.WINNER_DEAL_NOT_FOUND);
        }

        // 구매자가 아닌 경우
        if (!winnerDeal.getWinnerId().equals(memberId)) {
            throw new CustomException(ErrorType.WINNER_DEAL_SHIPPING_ADDRESS_ACCESS_DENIED);
        }

        // 배송지 또는 운송장 정보가 이미 있거나 PAID 상태가 아닌 경우
        if (winnerDeal.getStatus() != WinnerDealStatus.PAID
                || isShippingInfoRegistered(winnerDeal)
                || isTrackingNumberRegistered(winnerDeal)) {
            throw new CustomException(ErrorType.WINNER_DEAL_SHIPPING_ADDRESS_REGISTRATION_NOT_ALLOWED);
        }

        int updatedRows = winnerDealMapper.updateShippingAddress(winnerDealId, request);
        if (updatedRows != 1) {
            throw new CustomException(ErrorType.WINNER_DEAL_SHIPPING_ADDRESS_SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public void registerTrackingNumber(Long winnerDealId, Long memberId, WinnerDealTrackingNumberRequest request) {
        WinnerDeal winnerDeal = winnerDealMapper.findById(winnerDealId);

        if (winnerDeal == null) {
            throw new CustomException(ErrorType.WINNER_DEAL_NOT_FOUND);
        }

        // 판매자가 아닌 경우
        if (!winnerDeal.getSellerId().equals(memberId)) {
            throw new CustomException(ErrorType.WINNER_DEAL_TRACKING_NUMBER_ACCESS_DENIED);
        }

        // 운송장 등록은 PAID 상태에서 배송지 정보가 있고 아직 운송장 정보가 없을 때만 허용한다.
        if (winnerDeal.getStatus() != WinnerDealStatus.PAID
                || !isShippingInfoRegistered(winnerDeal)
                || isTrackingNumberRegistered(winnerDeal)) {
            throw new CustomException(ErrorType.WINNER_DEAL_TRACKING_NUMBER_REGISTRATION_NOT_ALLOWED);
        }

        int updatedRows = winnerDealMapper.updateTrackingNumber(winnerDealId, request);
        if (updatedRows != 1) {
            throw new CustomException(ErrorType.WINNER_DEAL_TRACKING_NUMBER_SAVE_FAILED);
        }

        notificationPublisher.publishNotification(
                winnerDeal.getWinnerId(),
                NotificationType.DELIVERY,
                "상품이 발송되었습니다. 운송사: " + request.getCarrier() + ", 송장번호 : " + request.getTrackingNumber(),
                "/winner-deals/" + winnerDealId
        );



    }

    @Override
    @Transactional
    public void confirmPurchase(Long winnerDealId, Long memberId) {
        WinnerDeal winnerDeal = winnerDealMapper.findById(winnerDealId);

        if (winnerDeal == null) {
            throw new CustomException(ErrorType.WINNER_DEAL_NOT_FOUND);
        }

        if (!winnerDeal.getWinnerId().equals(memberId)) {
            throw new CustomException(ErrorType.WINNER_DEAL_CONFIRM_ACCESS_DENIED);
        }

        // 구매확정은 구매자 본인의 배송 중 거래에 대해서만 1회 허용한다.
        if (winnerDeal.getStatus() != WinnerDealStatus.SHIPPED
                || winnerDeal.getConfirmedAt() != null) {
            throw new CustomException(ErrorType.WINNER_DEAL_CONFIRM_NOT_ALLOWED);
        }

        int updatedRows = winnerDealMapper.confirmPurchase(winnerDealId, LocalDateTime.now());
        if (updatedRows != 1) {
            throw new CustomException(ErrorType.WINNER_DEAL_CONFIRM_FAILED);
        }

        Long lastBidAmount = bidService.getLastBidAmount(memberId, winnerDeal.getAuctionId());
        if (lastBidAmount == null || lastBidAmount < winnerDeal.getWinnerPrice()) {
            throw new CustomException(ErrorType.WINNER_DEAL_SETTLEMENT_INVALID);
        }

        long refundAmount = lastBidAmount - winnerDeal.getWinnerPrice();
        if (refundAmount < 0) {
            throw new CustomException(ErrorType.WINNER_DEAL_SETTLEMENT_INVALID);
        } else if (refundAmount > 0) {
            pointService.refundBid(memberId, refundAmount);
        }

        pointService.settleWinnerDeal(winnerDeal.getSellerId(), winnerDeal.getWinnerPrice());
    }

    private void refundAndCancelWinnerDeal(WinnerDeal winnerDeal) {
        if (winnerDeal.getStatus() == WinnerDealStatus.CANCELLED) {
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

    private boolean isShippingInfoRegistered(WinnerDeal winnerDeal) {
        // null, 빈 문자열, 공백만 있는 값은 미입력으로 본다.
        return StringUtils.hasText(winnerDeal.getRecipient())
                && StringUtils.hasText(winnerDeal.getTel())
                && StringUtils.hasText(winnerDeal.getZipcode())
                && StringUtils.hasText(winnerDeal.getAddress());
    }

    private boolean isTrackingNumberRegistered(WinnerDeal winnerDeal) {
        // null, 빈 문자열, 공백만 있는 값은 미입력으로 본다.
        return StringUtils.hasText(winnerDeal.getCarrier())
                && StringUtils.hasText(winnerDeal.getTrackingNumber());
    }

    private String generateDealNumber() {
        for (int attempt = 0; attempt < DEAL_NUMBER_MAX_RETRY_COUNT; attempt++) {
            String datePart = LocalDateTime.now().format(DEAL_NUMBER_DATE_FORMATTER);
            String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
            String dealNumber = "WD-" + datePart + "-" + randomPart;

            if (!winnerDealMapper.existsByDealNumber(dealNumber)) {
                return dealNumber;
            }
        }

        throw new CustomException(ErrorType.WINNER_DEAL_UPDATE_FAILED);
    }
}

