package com.biddingmate.biddinggo.review.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.review.dto.CreateReviewRequest;
import com.biddingmate.biddinggo.review.dto.CreateReviewResponse;
import com.biddingmate.biddinggo.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auctions")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "경매 건에 대해 리뷰를 등록합니다.")
    @PostMapping("/{auctionId}/reviews")
    public ResponseEntity<ApiResponse<CreateReviewResponse>> createReview(
            @PathVariable Long auctionId,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        // TODO: 향후 토큰에서 유저 정보 추출하도록 변경 예정
        Long currentUserId = 1L;

        CreateReviewResponse result = reviewService.createReview(auctionId, currentUserId, request);

        return ApiResponse.of(HttpStatus.OK, null, "리뷰 등록 성공", result);
    }
}