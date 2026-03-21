package com.biddingmate.biddinggo.auctioninquiry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAuctionInquiryRequest {

    @NotBlank(message = "문의 내용은 필수입니다.")
    private String content;

}