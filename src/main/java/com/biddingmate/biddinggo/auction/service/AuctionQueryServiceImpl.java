package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.mapper.AuctionMybatisMapper;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.ItemImageMybatisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionQueryServiceImpl implements AuctionQueryService {
    private final AuctionMybatisMapper auctionMybatisMapper;
    private final ItemImageMybatisMapper itemImageMybatisMapper;

    @Override
    @Transactional(readOnly = true)
    public AuctionDetailResponse getAuctionDetail(Long auctionId) {
        if (auctionId == null || auctionId <= 0) {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }

        AuctionDetailResponse detail = auctionMybatisMapper.findDetailById(auctionId);

        if (detail == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        List<AuctionDetailResponse.Image> images = itemImageMybatisMapper.findDetailImagesByItemId(detail.getItem().getItemId());
        detail.getItem().setImages(images);

        return detail;
    }
}
