package com.biddingmate.biddinggo.directinquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateDirectInquiryRequest {
    private String category;
    private String content;
}
