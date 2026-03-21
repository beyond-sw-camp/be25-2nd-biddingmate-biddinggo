package com.biddingmate.biddinggo.auctioninquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateAuctionInquiryResponse {

    private Long id;
    private Long auctionId;
    private Long writerId;
    private String content;
    private LocalDateTime createdAt;
}