package com.biddingmate.biddinggo.winnerdeal.service;

import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;

import java.util.List;

public interface WinnerDealQueryService {
    // 낙찰 완료된 거래를 회원 기준으로 조회
    List<WinnerDeal> findByMemberId(Long memberId);
}
