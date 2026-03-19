package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.dto.CreateBidResponse;
import jakarta.validation.Valid;

public interface BidService {
    /**
     * actionId에 해당하는 경매에 입찰 데이터를 저장한다.
     */
    CreateBidResponse createBid(Long memberId, Long auctionId, @Valid CreateBidRequest request);
}
