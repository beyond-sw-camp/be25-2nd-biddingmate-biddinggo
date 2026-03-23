package com.biddingmate.biddinggo.wishlist.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.wishlist.model.Wishlist;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WishlistMapper extends IMybatisCRUD<Wishlist> {
    Long findByMemberIdAndAuctionId(Long memberId, Long auctionId);
}