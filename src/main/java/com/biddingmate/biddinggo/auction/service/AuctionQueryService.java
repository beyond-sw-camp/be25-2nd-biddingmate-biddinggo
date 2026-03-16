package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;

/**
 * 경매 조회 전용 서비스.
 * 등록/수정 로직과 분리하여 읽기 책임만 담당한다.
 */
public interface AuctionQueryService {
    /**
     * 경매 ID를 기준으로 상세 정보를 조회한다.
     */
    AuctionDetailResponse getAuctionDetail(Long auctionId);
}
