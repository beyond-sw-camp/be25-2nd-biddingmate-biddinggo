package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.auction.model.Auction;
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
    public Bid createBid(Long memberId, Auction auction, CreateBidRequest request) {
        Bid vickreyBid = bidMapper.getVickreyBid(auction.getId());
        Long basePrice = (vickreyBid == null) ? auction.getStartPrice() : vickreyBid.getAmount();
        if(basePrice + auction.getBidUnit() > request.getAmount()){
            // 입찰 금액이 현재 차순위 입찰가(없으면 시작가) + 입찰단위 미만인 경우

        }
        if((request.getAmount() - auction.getStartPrice()) % auction.getBidUnit() != 0){
            // 입찰 단위가 아닌 값이 입찰가로 들어온 경우
        }
        Long lastBidAmount = bidMapper.getLastBidAmountByMemberId(auction.getId(), memberId);
        if (lastBidAmount == null) {
            lastBidAmount = 0L;
        }

        if(lastBidAmount >= request.getAmount()){
            //이미 입찰한 금액보다 적거나 같은 경우
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
