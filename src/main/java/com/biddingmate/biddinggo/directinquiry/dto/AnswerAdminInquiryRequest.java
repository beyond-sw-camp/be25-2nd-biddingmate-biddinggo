package com.biddingmate.biddinggo.directinquiry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AnswerAdminInquiryRequest {
    @NotBlank(message = "답변 내용은 필수입니다.")
    private String answer;
}
