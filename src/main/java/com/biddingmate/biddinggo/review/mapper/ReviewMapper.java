package com.biddingmate.biddinggo.review.mapper;

import com.biddingmate.biddinggo.review.model.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
    // 리뷰 저장
    int insertReview(Review review);

    // 동일한 거래에 대해 이미 리뷰를 썼는지 확인용
    int countByDealIdAndWriterId(@Param("dealId") Long dealId, @Param("writerId") Long writerId);
}