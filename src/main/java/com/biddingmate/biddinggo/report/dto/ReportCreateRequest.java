package com.biddingmate.biddinggo.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {
    private Long auctionId;
    private Long targetMemberId;
    private String reason;
}