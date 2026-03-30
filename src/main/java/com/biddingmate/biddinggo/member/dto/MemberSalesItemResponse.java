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

    private String itemName;
    private String buyerName;
    private Long price;
    private LocalDateTime completedAt;
    private String deliveryStatus;
}
