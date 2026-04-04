package com.biddingmate.biddinggo.winnerdeal.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinnerDeal {
    private Long id;
    private Long auctionId;
    private Long winnerId;
    private Long sellerId;
    private Long winnerPrice;
    private String status;          // 기본값 'PAID'
    private String deliveryStatus;  // 추가: 기본값 'SHIPPED'
    private LocalDateTime confirmedAt;
    private String zipcode;
    private String address;
    private String detailAddress;
    private String tel;
    private String recipient;
    private String carrier;
    private String trackingNumber;
    private LocalDateTime createdAt;
}