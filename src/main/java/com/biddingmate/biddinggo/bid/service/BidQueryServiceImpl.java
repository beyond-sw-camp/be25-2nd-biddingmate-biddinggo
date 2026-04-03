package com.biddingmate.biddinggo.bid.service;

import com.biddingmate.biddinggo.bid.mapper.BidMapper;
import com.biddingmate.biddinggo.bid.model.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidQueryServiceImpl implements BidQueryService {
    private final BidMapper bidMapper;

    @Override
    @Transactional
    public List<Long> findOngoingAuctionIdsByMember(Long memberId) {
        // 특정 회원이 참여한 진행 중 경매 목록 조회
        return bidMapper.findOngoingAuctionIdsByMember(memberId);
    }

    @Override
    @Transactional
    public Long findTopBidderId(Long auctionId) {
        // 현재 최고 입찰자 조회 (전체 기준)
        // 현재 최고 입찰자가 비활성인지 확인하는 단계
        return bidMapper.findTopBidderId(auctionId);
    }

    @Override
    @Transactional
    public List<Bid> findTop2ActiveBids(Long auctionId) {
        // ACTIVE 회원 기준 상위 2개 입찰 조회 (비크리 핵심)
        return bidMapper.findTop2ActiveBids(auctionId);
    }
}
