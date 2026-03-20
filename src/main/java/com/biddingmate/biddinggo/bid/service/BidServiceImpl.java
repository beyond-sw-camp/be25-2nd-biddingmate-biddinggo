package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.model.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidMapper bidMapper;

    @Override
    @Transactional
    public Bid createBid(Long memberId, Long auctionId, CreateBidRequest request) {
        // Bid 등록
        Bid bid = Bid.builder()
                .bidderId(memberId)
                .auctionId(auctionId)
                .amount(request.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        int bidInsertCount = bidMapper.insert(bid);

        if(bidInsertCount != 1 || bid.getId() == null){
            //BID_SAVE_FAILED
        }

        return bid;
    }

    @Override
    public Long getLastBidAmount(Long memberId, Long auctionId) {
        Long lastBidAmount = bidMapper.getLastBidAmountByMemberId(auctionId, memberId);
        if (lastBidAmount == null) {
            lastBidAmount = 0L;
        }

        return lastBidAmount;
    }
}
