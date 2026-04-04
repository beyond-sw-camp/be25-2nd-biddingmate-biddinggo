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
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WinnerDealService {

    private final AuctionMapper auctionMapper;
    private final BidMapper bidMapper;
    private final WinnerDealMapper winnerDealMapper;
    private final AuctionItemMapper auctionItemMapper;

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

            log.info("낙찰 처리 성공 - Auction ID: {}, Winner: {}, Price: {}",
                    auction.getId(), winnerBid.getBidderId(), finalPrice);
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
}