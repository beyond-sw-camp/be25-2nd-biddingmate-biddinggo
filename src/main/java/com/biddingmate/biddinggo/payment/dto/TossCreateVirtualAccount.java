package com.biddingmate.biddinggo.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TossCreateVirtualAccount {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String status;
    private String method;
    private Long totalAmount;
    private String requestedAt;
    private VirtualAccount virtualAccount;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class VirtualAccount {
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
    }
}
