package com.biddingmate.biddinggo.auction.event;

import com.biddingmate.biddinggo.auction.service.AuctionService;
import com.biddingmate.biddinggo.member.event.MemberStatusUpdateEvent;
import com.biddingmate.biddinggo.member.model.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class AuctionMemberStatusListener {

    private final AuctionService auctionService;

    @EventListener
    @Transactional
    public void handleMemberStatusUpdated(MemberStatusUpdateEvent event) {
        if (event.getStatus() == MemberStatus.INACTIVE) {
            Long memberId = event.getMemberId();

            // 비활성화된 판매자의 경매 취소
            // 진행 중 경매 조회
            List<Long> auctionIds = auctionService.findActiveAuctionsBySeller(memberId);
            auctionService.cancelAuctionsAndItems(auctionIds);

            // 최고 입찰자의 비활성화 -> 비크리 재계산
            auctionService.recalculateVickreyPriceByBidder(memberId);
        }
    }
}
