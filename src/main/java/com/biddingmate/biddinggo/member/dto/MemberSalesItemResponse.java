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
public class MemberSalesItemResponse {

    // 상품 대표 이미지 URL
    private String imageUrl;

    // 상품명
    private String itemName;

    // 거래 상태
    private String status;

    // 판매 금액
    private Long winnerPrice;

    // 거래 생성일
    private LocalDateTime createdAt;
}
