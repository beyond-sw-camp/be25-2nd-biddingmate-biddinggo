package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberBiddingItemResponse {
    private String imageUrl;
    private String itemName;
    private Long currentPrice;
    private Long myBidPrice;
    private LocalDateTime endDate;
}
