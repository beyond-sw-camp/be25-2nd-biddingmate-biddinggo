package com.biddingmate.biddinggo.auction.mapper;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.dto.AuctionListResponse;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuctionMapper extends IMybatisCRUD<Auction> {
    AuctionDetailResponse findDetailById(Long auctionId);

    List<AuctionListResponse> findAuctionList(RowBounds rowBounds,
                                              @Param("status") AuctionStatus status,
                                              @Param("sellerId") Long sellerId,
                                              @Param("categoryId") Long categoryId,
                                              @Param("sortBy") String sortBy,
                                              @Param("order") String order);

    int countAuctionList(@Param("status") AuctionStatus status,
                         @Param("sellerId") Long sellerId,
                         @Param("categoryId") Long categoryId);

    int updateAuction(Auction auction);

    int cancelAuction(@Param("auctionId") Long auctionId,
                      @Param("cancelDate") LocalDateTime cancelDate,
                      @Param("newStatus") AuctionStatus newStatus);

    void updateAfterBid(@Param("id") Long id, @Param("vickreyPrice") Long vickreyPrice);

    Auction findById(Long auctionId);

    Auction findByIdForUpdate(Long auctionId);

    void updateWishCount(@Param("id") Long id, @Param("wishCount") int wishCount);

    List<Long> findActiveAuctionIdsBySeller(@Param("sellerId") Long memberId);


    void updateAuctionStatus(@Param("auctionIds") List<Long> auctionIds,
                             @Param("status") AuctionStatus status);

    void resetVickreyPriceToStartPrice(@Param("auctionId") Long auctionId);

    void updateVickreyPrice(@Param("auctionId") Long auctionId,
                            @Param("amount") Long amount);
}
