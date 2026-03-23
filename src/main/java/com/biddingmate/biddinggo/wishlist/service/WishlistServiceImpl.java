package com.biddingmate.biddinggo.wishlist.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistResponse;
import com.biddingmate.biddinggo.wishlist.mapper.WishlistMapper;
import com.biddingmate.biddinggo.wishlist.model.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final AuctionMapper auctionMapper;
    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional
    public CreateWishlistResponse createWishlist(Long memberId, Long auctionId) {
        if(auctionMapper.findById(auctionId) == null){
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        if(wishlistMapper.findByMemberIdAndAuctionId(memberId, auctionId) != null){
            throw new CustomException(ErrorType.WISHLIST_ALREADY_EXISTS);
        }

        Wishlist wishlist = Wishlist.builder()
                .memberId(memberId)
                .auctionId(auctionId)
                .createdAt(LocalDateTime.now())
                .build();

        int insert = wishlistMapper.insert(wishlist);

        if (insert <= 0) {
            throw new CustomException(ErrorType.WISHLIST_SAVE_FAIL);
        }

        return CreateWishlistResponse.builder()
                .id(wishlist.getId())
                .memberId(wishlist.getMemberId())
                .auctionId(wishlist.getAuctionId())
                .createdAt(wishlist.getCreatedAt())
                .build();
    }
}
