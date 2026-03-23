package com.biddingmate.biddinggo.wishlist.service;

import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistResponse;

public interface WishlistService {
    CreateWishlistResponse createWishlist(Long memberId, Long auctionId);
}
