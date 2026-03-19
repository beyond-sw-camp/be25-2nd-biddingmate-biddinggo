package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.dto.CreateBidResponse;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidMapper bidMapper;
    private final AuctionMapper auctionMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public CreateBidResponse createBid(Long memberId, Long auctionId, CreateBidRequest request) {

        Bid bid = Bid.builder()
                .bidderId(memberId)
                .auctionId(auctionId)
                .amount(request.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        int bidInsertCount = bidMapper.insert(bid);

        return CreateBidResponse.builder()
                .bidId(bid.getId())
                .build();
    }
}
