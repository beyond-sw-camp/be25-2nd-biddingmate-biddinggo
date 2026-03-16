package com.biddingmate.biddinggo.auction.mapper;

import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuctionMybatisMapper extends IMybatisCRUD<Auction> {
}
