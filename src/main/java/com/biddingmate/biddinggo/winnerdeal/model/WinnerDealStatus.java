package com.biddingmate.biddinggo.winnerdeal.model;

public enum WinnerDealStatus {
    PAID,                // 발송 대기
    SHIPPING,            // 배송 중
    DELIVERED,           //  배송 완료
    CONFIRMED,           // 거래 완료
    CANCELLED;           // 취소
}
