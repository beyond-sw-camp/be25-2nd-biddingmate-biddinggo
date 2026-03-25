package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDashboardResponse {
    private Long totalPurchaseAmount;

    private Long totalSalesAmount;

    private Long point;
}
