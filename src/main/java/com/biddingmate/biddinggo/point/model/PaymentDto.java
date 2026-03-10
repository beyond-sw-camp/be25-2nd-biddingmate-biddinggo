package com.biddingmate.biddinggo.point.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private Long memberId;
    private String orderId;
    private String paymentKey;
    private String paymentMethod;
    private Long amount;
    private PaymentStatus status;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}
