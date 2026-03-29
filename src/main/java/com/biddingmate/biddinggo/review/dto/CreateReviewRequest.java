package com.biddingmate.biddinggo.review.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {
    @NotNull(message = "별점은 필수입니다.")
    @Min(1) @Max(5) // 별점 1~5점 제한
    private Integer rating;

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    @Size(max = 500)
    private String content;

}
