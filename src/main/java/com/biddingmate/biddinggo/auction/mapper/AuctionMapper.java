package com.biddingmate.biddinggo.auction.mapper;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface AuctionMapper extends IMybatisCRUD<Auction> {
    AuctionDetailResponse findDetailById(Long auctionId);

    int updateAuction(Auction auction);

    int cancelAuction(@Param("auctionId") Long auctionId,
                      @Param("cancelDate") LocalDateTime cancelDate,
                      @Param("newStatus") AuctionStatus newStatus);

    void updateAfterBid(@Param("id") Long id, @Param("vickreyPrice") Long vickreyPrice);

    Auction findById(Long auctionId);

    Auction findByIdForUpdate(Long auctionId);

    void updateWishCount(@Param("id") Long id, @Param("wishCount") int wishCount);
}
