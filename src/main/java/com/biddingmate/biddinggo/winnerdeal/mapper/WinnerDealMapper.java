package com.biddingmate.biddinggo.winnerdeal.mapper;

import com.biddingmate.biddinggo.winnerdeal.model.WinnerDeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WinnerDealMapper {

    int insert(WinnerDeal winnerDeal);

    WinnerDeal findByAuctionId(Long auctionId);

}