package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionFromInspectionItemRequest;
import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.auction.dto.UpdateAuctionRequest;

/**
 * auction 테이블 저장 책임을 담당하는 서비스.
 */
public interface AuctionService {
    /**
     * 이미 생성된 itemId를 기준으로 auction 데이터를 저장한다.
     */
    Long createAuction(CreateAuctionRequest request, Long itemId, Long sellerId);

    /**
     * 검수 완료된 기존 itemId를 기준으로 auction 데이터를 저장한다.
     */
    Long createAuction(CreateAuctionFromInspectionItemRequest request, Long sellerId);

    /**
     * 경매 정보를 수정한다.
     */
    void updateAuction(Long auctionId, UpdateAuctionRequest request, Long sellerId);

    /**
     * 경매를 취소 처리한다.
     */
    void cancelAuction(Long auctionId, Long sellerId);

    /**
     * 寃쎈ℓ瑜?利됱떆 援щℓ濡?醫낅즺?쒕떎.
     */
    void buyNowAuction(Long auctionId, Long buyerId);

    // 유저 비활성화 이벤트
    void handleMemberDeactivationBeforeWinning(Long memberId);
}
