package com.biddingmate.biddinggo.review.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.review.dto.CreateReviewRequest;
import com.biddingmate.biddinggo.review.dto.CreateReviewResponse;
import com.biddingmate.biddinggo.review.mapper.ReviewMapper;
import com.biddingmate.biddinggo.review.model.Review;
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final AuctionMapper auctionMapper;
    private final WinnerDealMapper winnerDealMapper;

    @Override
    @Transactional
    public CreateReviewResponse createReview(Long auctionId, Long writerId, CreateReviewRequest request) {

        // 옥션 정보 조회
        Auction auction = auctionMapper.findById(auctionId);
        if (auction == null) throw new CustomException(ErrorType.AUCTION_NOT_FOUND);

        // 권한 체크
        if (!writerId.equals(auction.getWinnerId())) {
            throw new CustomException(ErrorType.NOT_THE_WINNER);
        }
        // 낙찰 정보 조회
        WinnerDeal deal = winnerDealMapper.findByAuctionId(auctionId);
        if (deal == null) {
            throw new CustomException(ErrorType.DEAL_NOT_FOUND); // 낙찰 데이터가 없으면 리뷰 불가
        }
        // 중복 리뷰 방지
        int count = reviewMapper.countByDealIdAndWriterId(deal.getId(), writerId);
        if (count > 0) {
            throw new CustomException(ErrorType.ALREADY_REVIEWED);
        }
        // 리뷰 저장
        Review review = Review.builder()
                .dealId(deal.getId())
                .writerId(writerId)
                .targetId(auction.getSellerId())
                .rating(request.getRating())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        reviewMapper.insertReview(review);

        return CreateReviewResponse.builder()
                .id(review.getId())
                .auctionId(auctionId)
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
