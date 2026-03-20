package com.biddingmate.biddinggo.bid.mapper;

import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BidMapper extends IMybatisCRUD<Bid> {

    Bid getVickreyBid(@Param("auctionId") Long auctionId);

    Long getLastBidAmountByMemberId(@Param("auctionId") Long auctionId,
                              @Param("bidderId") Long bidderId);
}
