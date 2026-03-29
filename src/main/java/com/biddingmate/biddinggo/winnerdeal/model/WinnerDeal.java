package com.biddingmate.biddinggo.winnerdeal.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinnerDeal {
    private Long id;              // 낙찰 PK
    private Long auctionId;       // 옥션 FK (3번)
    private Long winnerId;        // 낙찰자(구매자) ID
    private Long sellerId;        // 판매자 ID
    private Long winnerPrice;     // 낙찰가
    private String status;        // 상태 (PAID 등)
    private LocalDateTime createdAt;
}