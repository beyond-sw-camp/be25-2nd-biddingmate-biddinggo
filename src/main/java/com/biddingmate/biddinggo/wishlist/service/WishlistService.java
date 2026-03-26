package com.biddingmate.biddinggo.wishlist.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistRequest;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistResponse;

public interface WishlistService {
    CreateWishlistResponse createWishlist(CreateWishlistRequest request, Long memberId);

    PageResponse<AuctionDetailResponse> findWishlistAuctionsByMemberId(BasePageRequest request, Long memberId);

    int deleteWishlist(CreateWishlistRequest request, Long memberId);
}
