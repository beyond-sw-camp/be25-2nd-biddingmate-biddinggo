package com.biddingmate.biddinggo.item.service;

import com.biddingmate.biddinggo.item.dto.AuctionItemCreateSource;

/**
 * auction_item 테이블 저장 책임을 담당하는 서비스.
 */
public interface AuctionItemService {
    /**
     * 경매 등록용 auction_item을 생성한다.
     *
     * <p>{@code auction_item.inspection_status}는 이 단계에서 명시하지 않고,
     * DB 기본값({@code NONE})을 사용한다.</p>
     */
    Long createAuctionItem(AuctionItemCreateSource item);

    /**
     * 검수 등록용 auction_item을 생성한다.
     *
     * <p>검수 등록은 일반 경매 등록과 달리,
     * {@code auction_item.status = PENDING},
     * {@code auction_item.inspection_status = PENDING}을 명시 저장한다.</p>
     */
    Long createInspectionItem(AuctionItemCreateSource item);
}
