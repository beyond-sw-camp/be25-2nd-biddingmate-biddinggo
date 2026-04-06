package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryResponse;
import jakarta.validation.Valid;

public interface WinnerDealService {
    void processClosing(Auction auction);
    void handleMemberDeactivationAfterWinning(Long memberId);
}
