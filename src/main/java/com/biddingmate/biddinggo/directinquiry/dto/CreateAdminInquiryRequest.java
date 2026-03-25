package com.biddingmate.biddinggo.directinquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateAdminInquiryRequest {
    private String category;
    private String content;
}
