package com.biddingmate.biddinggo.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString // 로그 확인용
public class TossPaymentOrderDetails {
    private String paymentKey;      // 결제 고유 키
    private String orderId;         // 우리 서버 주문 번호
    private String status;          // 최종 상태 (예: DONE)
    private Long totalAmount;       // 총 결제 금액
    private String method;          // 결제 수단 (카드, 가상계좌 등)
    private String approvedAt;      // 입금/승인 완료 시각
    private String requestedAt;     // 결제 요청 시각

    // 가상계좌의 경우, 입금 기한이나 계좌번호를 다시 확인해야 할 수도 있으므로 포함
    private VirtualAccount virtualAccount;

    @Getter
    @NoArgsConstructor
    public static class VirtualAccount {
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
    }
}
