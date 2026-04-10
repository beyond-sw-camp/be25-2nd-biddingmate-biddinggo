package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberWonItemResponse {

    // 상품 대표 이미지 URL
    private String imageUrl;

    // 상품명
    private String itemName;

    // 거래 상태
    private String status;

    // 낙찰 금액 (구매 금액)
    private Long winnerPrice;

    // 구매 일시 (거래 생성 시점)
    private LocalDateTime createdAt;
}
