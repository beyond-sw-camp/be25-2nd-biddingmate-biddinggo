package com.biddingmate.biddinggo.winnerdeal.mapper;

import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WinnerDealMapper {
    // 옥션 ID를 주면 낙찰 정보를 반환합니다.
    WinnerDeal findByAuctionId(Long auctionId);
}