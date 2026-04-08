package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealShippingAddressRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealTrackingNumberRequest;

public interface WinnerDealService {
    void processClosing(Auction auction);
    void handleMemberDeactivationAfterWinning(Long memberId);

    // 구매자 배송지 등록
    void registerShippingAddress(Long winnerDealId, Long memberId, WinnerDealShippingAddressRequest request);
    // 판매자 운송장 번호 등록
    void registerTrackingNumber(Long winnerDealId, Long memberId, WinnerDealTrackingNumberRequest request);
}
