package com.biddingmate.biddinggo.wishlist.mapper;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.wishlist.model.Wishlist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface WishlistMapper extends IMybatisCRUD<Wishlist> {
    Long findByMemberIdAndAuctionId(@Param("memberId") Long memberId, @Param("auctionId") Long auctionId);

    int getCountByAuctionId(@Param("auctionId") Long auctionId);

    List<AuctionDetailResponse> findWishlistAuctionsByMemberId (RowBounds rowBounds,
                                                                @Param("memberId") Long memberId,
                                                                @Param("order") String sortOrder);

    int getCountByMemberId(@Param("memberId") Long memberId);

    int delete(@Param("auctionId") Long auctionId, @Param("memberId")Long memberId);
}