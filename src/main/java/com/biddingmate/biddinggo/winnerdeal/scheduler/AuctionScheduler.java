package com.biddingmate.biddinggo.winnerdeal.scheduler;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.winnerdeal.service.WinnerDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionMapper auctionMapper;
    private final WinnerDealService winnerDealService;

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void executeClosing() {
        log.info("경매 마감 스케줄러 가동: {}", LocalDateTime.now());

        // 현재 진행 중인 경매 조회
        List<Auction> expiredAuctions = auctionMapper.findExpiredAuctions(
                LocalDateTime.now(),
                AuctionStatus.ON_GOING
        );

        for (Auction auction : expiredAuctions) {
            try {
                winnerDealService.processClosing(auction);
            } catch (Exception e) {
                log.error("경매 종료 처리 중 에러 발생 (ID: {}): {}",
                        auction.getId(), e.getMessage());
            }
        }
    }
}