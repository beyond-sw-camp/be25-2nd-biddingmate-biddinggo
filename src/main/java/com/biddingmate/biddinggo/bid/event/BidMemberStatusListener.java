package com.biddingmate.biddinggo.bid.event;

import com.biddingmate.biddinggo.bid.service.BidService;
import com.biddingmate.biddinggo.member.event.MemberStatusUpdateEvent;
import com.biddingmate.biddinggo.member.model.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Order(2)  // Auction 처리 후 실행
public class BidMemberStatusListener {

    private final BidService bidService;

    @EventListener
    @Transactional
    public void handleMemberStatusUpdated(MemberStatusUpdateEvent event) {
        if (event.getStatus() == MemberStatus.INACTIVE) {
//            bidService.invalidateBidsByMember(event.getMemberId());
        }
    }
}
