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

            // 진행 중 경매 조회
            List<Long> auctionIds = auctionService.findActiveAuctionsBySeller(memberId);

            // Item 도메인 서비스로 상태 변경 위임
            auctionService.cancelAuctionsAndItems(auctionIds);
        }
    }
}
