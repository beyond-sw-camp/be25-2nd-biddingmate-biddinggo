package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.ItemImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 경매 상세 조회 구현체.
 * 본문 정보와 이미지 목록을 분리 조회한 뒤 하나의 응답 DTO로 조합한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionQueryServiceImpl implements AuctionQueryService {
    private final AuctionMapper auctionMapper;
    private final ItemImageMapper itemImageMapper;

    @Override
    @Transactional(readOnly = true)
    /**
     * 경매 상세 조회 메인 로직.
     * 1) 경매/상품/카테고리 본문 조회
     * 2) item_image 목록 조회
     * 3) 이미지 목록을 응답 객체에 조합
     */
    public AuctionDetailResponse getAuctionDetail(Long auctionId) {
        if (auctionId == null || auctionId <= 0) {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }

        // 경매 기본 정보와 상품 본문 정보를 먼저 조회한다.
        AuctionDetailResponse detail = auctionMapper.findDetailById(auctionId);

        if (detail == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        // item_id 기준으로 이미지 목록을 별도 조회해 노출 순서대로 붙인다.
        List<AuctionDetailResponse.Image> images = itemImageMapper.findDetailImagesByItemId(detail.getItem().getItemId());
        detail.getItem().setImages(images);

        return detail;
    }
}
