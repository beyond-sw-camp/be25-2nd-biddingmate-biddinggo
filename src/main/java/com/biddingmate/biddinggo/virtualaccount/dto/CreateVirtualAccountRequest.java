package com.biddingmate.biddinggo.virtualaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateVirtualAccountRequest {
    // 토스에 넘길 자료
    private String orderId;
    private String orderName;
    private Long amount;
    private String customerName;
    private String bank;
}
