package com.biddingmate.biddinggo.auction.mapper;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuctionMapper extends IMybatisCRUD<Auction> {
    AuctionDetailResponse findDetailById(Long auctionId);

    void updateAfterBid(@Param("id") Long id, @Param("vickreyPrice") Long vickreyPrice);

    Auction findById(Long auctionId);

    Auction findByIdForUpdate(Long auctionId);
}
