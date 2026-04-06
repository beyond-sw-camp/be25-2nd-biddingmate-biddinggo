package com.biddingmate.biddinggo.winnerdeal.mapper;

import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WinnerDealMapper {
    int insert(WinnerDeal winnerDeal);
    WinnerDeal findByAuctionId(Long auctionId);

    List<WinnerDeal> findByMemberId(@Param("memberId") Long memberId);

    int updateStatus(@Param("id") Long id,
                     @Param("status") String status);
}
