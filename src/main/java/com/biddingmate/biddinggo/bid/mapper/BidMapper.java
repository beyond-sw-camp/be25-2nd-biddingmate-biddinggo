package com.biddingmate.biddinggo.bid.mapper;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.model.Bid;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidMapper extends IMybatisCRUD<Bid> {

    Bid getVickreyBid(@Param("auctionId") Long auctionId);

    Long getLastBidAmountByMemberId(@Param("auctionId") Long auctionId,
                                    @Param("bidderId") Long bidderId);

    int getBidCount(Map<String, Object> params);

    List<BidResponse> getBidsByAuctionId(RowBounds rowBounds,
                                         @Param("auctionId") Long auctionId,
                                         @Param("order") String sortOrder);

    List<AuctionDetailResponse> findBidAuctionsByMemberId(RowBounds rowBounds,
                                                          @Param("memberId") Long memberId,
                                                          @Param("order")String sortOrder);
}
