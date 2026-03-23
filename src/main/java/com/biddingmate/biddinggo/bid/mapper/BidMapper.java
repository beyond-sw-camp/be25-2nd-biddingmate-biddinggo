package com.biddingmate.biddinggo.bid.mapper;

import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface BidMapper extends IMybatisCRUD<Bid> {

    Bid getVickreyBid(@Param("auctionId") Long auctionId);

    Long getLastBidAmountByMemberId(@Param("auctionId") Long auctionId,
                                    @Param("bidderId") Long bidderId);

    Long getBidCount(@Param("auctionId") Long auctionId);

    List<BidResponse> getBidsByAuctionId(RowBounds rowBounds,
                                         @Param("auctionId") Long auctionId,
                                         @Param("order") String sortOrder);

    int getBidsByAuctionIdCount(@Param("auctionId") Long auctionId);
}
