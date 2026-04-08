package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.winnerdeal.dto.RegisterWinnerDealShippingAddressRequest;

public interface WinnerDealService {
    void processClosing(Auction auction);
    void handleMemberDeactivationAfterWinning(Long memberId);

    // 구매자 배송지 등록
    void registerShippingAddress(Long winnerDealId, Long memberId, RegisterWinnerDealShippingAddressRequest request);
}
