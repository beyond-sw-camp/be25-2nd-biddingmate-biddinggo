package com.biddingmate.biddinggo.bid.mapper;

import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BidMapper extends IMybatisCRUD<Bid> {
}
