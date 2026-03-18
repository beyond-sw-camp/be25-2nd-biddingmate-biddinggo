package com.biddingmate.biddinggo.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TossDepositWebhook {
    private String createdAt;      // 웹훅 생성 시간
    private String secret;         // 가상계좌 검증 값
    private String status;         // 결제 상태 (DONE 등)
    private String transactionKey; // 거래 고유 키
    private String orderId;        // 주문 번호

    @Override
    public String toString() {
        return "TossDepositWebhookRequest{" +
                "createdAt='" + createdAt + '\'' +
                ", secret='" + secret + '\'' +
                ", status='" + status + '\'' +
                ", transactionKey='" + transactionKey + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
