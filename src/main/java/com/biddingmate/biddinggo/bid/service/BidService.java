package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.model.Bid;
import jakarta.validation.Valid;

public interface BidService {
    /**
     * actionId에 해당하는 경매에 입찰 데이터를 저장한다.
     */
    Bid createBid(Long memberId, Auction auction, @Valid CreateBidRequest request);

    Long getLastBidAmount(Long memberId, Long auctionId);
}
