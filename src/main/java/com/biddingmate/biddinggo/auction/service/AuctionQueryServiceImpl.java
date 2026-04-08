package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.dto.AuctionListRequest;
import com.biddingmate.biddinggo.auction.dto.AuctionListResponse;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.auction.prediction.model.AuctionPricePredictionQuery;
import com.biddingmate.biddinggo.auction.prediction.service.AuctionPricePredictionService;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.item.mapper.ItemImageMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
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
    private static final String DEFAULT_SORT_BY = "CREATED_AT";
    private static final String SORT_BY_WISH_COUNT = "WISH_COUNT";
    private static final String SORT_BY_POPULARITY = "POPULARITY";
    private static final String SORT_BY_PRICE = "PRICE";

    private final AuctionMapper auctionMapper;
    private final ItemImageMapper itemImageMapper;
    private final AuctionPricePredictionService auctionPricePredictionService;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuctionListResponse> getAuctionList(AuctionListRequest request) {
        String order = request.getOrder();

        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            throw new CustomException(ErrorType.INVALID_SORT_ORDER);
        }

        AuctionStatus status = parseAuctionStatus(request.getStatus());
        String sortBy = parseSortBy(request.getSortBy());
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());
        String sortOrder = order.toUpperCase();

        List<AuctionListResponse> list = auctionMapper.findAuctionList(
                rowBounds,
                status,
                request.getSellerId(),
                request.getCategoryId(),
                sortBy,
                sortOrder
        );
        int count = auctionMapper.countAuctionList(status, request.getSellerId(), request.getCategoryId());

        return PageResponse.of(list, request.getPage(), request.getSize(), count);
    }

    private AuctionStatus parseAuctionStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return AuctionStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }
    }

    private String parseSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_BY;
        }

        String normalizedSortBy = sortBy.trim().toUpperCase();

        if (!DEFAULT_SORT_BY.equals(normalizedSortBy)
                && !SORT_BY_WISH_COUNT.equals(normalizedSortBy)
                && !SORT_BY_POPULARITY.equals(normalizedSortBy)
                && !SORT_BY_PRICE.equals(normalizedSortBy)) {
            throw new CustomException(ErrorType.INVALID_SORT_BY);
        }

        return normalizedSortBy;
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * 경매 상세 조회 메인 로직.
     * 1) 경매/상품/카테고리 본문 조회
     * 2) item_image 목록 조회
     * 3) 이미지 목록을 응답 객체에 조합
     * 4) 저장된 query embedding과 낙찰 reference를 바탕으로 예측가를 계산해 응답에 주입
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

        detail.setPricePrediction(auctionPricePredictionService.predict(
                AuctionPricePredictionQuery.builder()
                        .auctionId(detail.getAuctionId())
                        .categoryId(extractCategoryId(detail))
                        .quality(detail.getItem() != null ? detail.getItem().getQuality() : null)
                        .build()
        ));

        return detail;
    }

    private Long extractCategoryId(AuctionDetailResponse detail) {
        if (detail == null || detail.getItem() == null || detail.getItem().getCategory() == null) {
            return null;
        }

        return detail.getItem().getCategory().getId();
    }
}
