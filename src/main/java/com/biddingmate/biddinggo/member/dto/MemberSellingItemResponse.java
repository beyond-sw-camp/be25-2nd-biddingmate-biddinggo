package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSellingItemResponse {

    // 상품명
    private String itemName;

    // 현재 최고가
    private Long currentPrice;

    // 시작가
    private Long startPrice;

    // 입찰 수
    private Integer bidCount;

    // 남은 시간(초)
    private Long remainingSeconds;

    // 경매 타입
    private String auctionType;

    // 대표 이미지 URL
    private String imageUrl;
}
