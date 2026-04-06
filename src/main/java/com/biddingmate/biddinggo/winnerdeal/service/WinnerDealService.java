package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.model.Auction;

public interface WinnerDealService {
    void processClosing(Auction auction);
    void handleMemberDeactivationAfterWinning(Long memberId);
}
