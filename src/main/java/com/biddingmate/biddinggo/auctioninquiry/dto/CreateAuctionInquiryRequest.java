package com.biddingmate.biddinggo.auctioninquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAuctionInquiryRequest {

    @Schema(description = "문의 내용", example = "상품 상태가 어떤가요?", required = true)
    @NotBlank(message = "문의 내용은 필수입니다.")
    private String content;
}