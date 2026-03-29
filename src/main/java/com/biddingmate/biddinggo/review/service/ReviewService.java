package com.biddingmate.biddinggo.review.service;

import com.biddingmate.biddinggo.review.dto.CreateReviewRequest;
import com.biddingmate.biddinggo.review.dto.CreateReviewResponse;
//import com.biddingmate.biddinggo.review.dto.ReviewView;

public interface ReviewService {
    // 리뷰 등록
    CreateReviewResponse createReview(Long auctionId, Long writerId, CreateReviewRequest request);
}