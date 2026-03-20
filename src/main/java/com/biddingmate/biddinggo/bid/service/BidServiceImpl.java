package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidMapper bidMapper;
    @Override
    public Bid createBid(Long memberId, Auction auction, CreateBidRequest request) {

        Long bidCount = bidMapper.getBidCount(auction.getId());
        Long minBidAmount;

        if (bidCount == 0) {
            minBidAmount = auction.getStartPrice();
        } else if (bidCount == 1) {
            minBidAmount = auction.getStartPrice() + auction.getBidUnit();
        } else {
            Bid vickreyBid = bidMapper.getVickreyBid(auction.getId());
            minBidAmount = vickreyBid.getAmount() + auction.getBidUnit();
        }

        if (minBidAmount > request.getAmount()) {
            throw new CustomException(ErrorType.BID_AMOUNT_TOO_LOW);
        }

        if((request.getAmount() - auction.getStartPrice()) % auction.getBidUnit() != 0){
            throw new CustomException(ErrorType.INVALID_BID_UNIT);
        }

        Long lastBidAmount = bidMapper.getLastBidAmountByMemberId(auction.getId(), memberId);
        if (lastBidAmount == null) {
            lastBidAmount = 0L;
        }

        if(lastBidAmount >= request.getAmount()){
            throw new CustomException(ErrorType.BID_AMOUNT_NOT_HIGHER_THAN_PREVIOUS);
        }

        // Bid 등록
        Bid bid = Bid.builder()
                .bidderId(memberId)
                .auctionId(auction.getId())
                .amount(request.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        int bidInsertCount = bidMapper.insert(bid);

        if(bidInsertCount != 1 || bid.getId() == null){
            throw new CustomException(ErrorType.BID_SAVE_FAILED);
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
