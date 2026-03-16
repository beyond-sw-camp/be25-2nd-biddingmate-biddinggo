package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;

public interface AuctionQueryService {
    AuctionDetailResponse getAuctionDetail(Long auctionId);
}
