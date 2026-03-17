package com.biddingmate.biddinggo.item.service;

import com.biddingmate.biddinggo.item.dto.AuctionItemCreateSource;

/**
 * auction_item 테이블 저장 책임을 담당하는 서비스.
 */
public interface AuctionItemService {
    /**
     * 경매 등록 요청에서 상품 정보만 분리하여 auction_item을 생성한다.
     */
    Long createAuctionItem(AuctionItemCreateSource item);

    /**
     * 검수 등록용 상품을 생성한다.
     */
    Long createInspectionItem(AuctionItemCreateSource item);
}
