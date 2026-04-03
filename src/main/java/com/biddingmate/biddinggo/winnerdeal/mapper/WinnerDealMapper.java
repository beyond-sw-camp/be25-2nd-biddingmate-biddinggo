package com.biddingmate.biddinggo.winnerdeal.mapper;

import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WinnerDealMapper {

    int insert(WinnerDeal winnerDeal);

    WinnerDeal findByAuctionId(Long auctionId);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}