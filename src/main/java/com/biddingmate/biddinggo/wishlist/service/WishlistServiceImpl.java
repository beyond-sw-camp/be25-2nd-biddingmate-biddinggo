package com.biddingmate.biddinggo.wishlist.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistRequest;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistResponse;
import com.biddingmate.biddinggo.wishlist.mapper.WishlistMapper;
import com.biddingmate.biddinggo.wishlist.model.Wishlist;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final AuctionMapper auctionMapper;
    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional
    public CreateWishlistResponse createWishlist(CreateWishlistRequest request, Long memberId) {
        Long auctionId = request.getAuctionId();
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

        auctionMapper.updateWishCount(auctionId, 1);

        return CreateWishlistResponse.builder()
                .id(wishlist.getId())
                .memberId(wishlist.getMemberId())
                .auctionId(wishlist.getAuctionId())
                .createdAt(wishlist.getCreatedAt())
                .build();
    }

    @Override
    public PageResponse<AuctionDetailResponse> findWishlistAuctionsByMemberId(BasePageRequest request, Long memberId) {
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());
        String order = request.getOrder();

        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            throw new CustomException(ErrorType.INVALID_SORT_ORDER);
        }
        String sortOrder = order.toUpperCase();

        List<AuctionDetailResponse> auctions = wishlistMapper.findWishlistAuctionsByMemberId(rowBounds, memberId, sortOrder);
        int count = wishlistMapper.getCountByMemberId(memberId);

        return PageResponse.of(auctions, request.getPage(), request.getSize(), count);
    }

    @Override
    @Transactional
    public int deleteWishlist(CreateWishlistRequest request, Long memberId) {
        Long auctionId = request.getAuctionId();
        if(auctionMapper.findById(auctionId) == null){
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        if(wishlistMapper.findByMemberIdAndAuctionId(memberId, auctionId) == null){
            throw new CustomException(ErrorType.WISHLIST_NOT_FOUND);
        }

        int delete = wishlistMapper.delete(auctionId, memberId);
        if (delete <= 0) {
            throw new CustomException(ErrorType.WISHLIST_DELETE_FAIL);
        }

        auctionMapper.updateWishCount(auctionId, -1);

        return delete;
    }
}
