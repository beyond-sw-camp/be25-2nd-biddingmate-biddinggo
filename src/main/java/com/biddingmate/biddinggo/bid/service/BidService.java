package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface BidService {
    /**
     * actionId에 해당하는 경매에 입찰 데이터를 저장한다.
     */
    Bid createBid(Long memberId, Auction auction, @Valid CreateBidRequest request);

    Long getLastBidAmount(Long memberId, Long auctionId);

    Bid getVickreyBid(Long auctionId);

    PageResponse<BidResponse> getBidsByAuctionId(BasePageRequest request, Long auctionId);

    PageResponse<AuctionDetailResponse> getBidAuctionsByMemberId(BasePageRequest request, Long memberId);

    void invalidateBidsByMember(Long memberId);
}
