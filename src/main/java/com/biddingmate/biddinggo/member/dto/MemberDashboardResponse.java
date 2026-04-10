package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDashboardResponse {

    // 대시보드에 노출될 회원 정보
    private String nickname;
    private String grade;
    private Long point;

    // 낙찰된 물품 목록
    private List<MemberWonItemResponse> wonItems;

    // 입찰 중 물품 목록
    private List<MemberBiddingItemResponse> biddingItems;

}