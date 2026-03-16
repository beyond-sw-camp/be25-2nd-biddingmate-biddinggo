package com.biddingmate.biddinggo.item.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.AuctionItemMapper;
import com.biddingmate.biddinggo.item.mapper.CategoryMapper;
import com.biddingmate.biddinggo.item.model.AuctionItem;
import com.biddingmate.biddinggo.item.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * auction_item 엔티티 생성만 담당하는 서비스 구현체.
 * 경매 등록 시 선택한 카테고리가 실제 최하위 카테고리인지 함께 검증한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionItemServiceImpl implements AuctionItemService {
    private final AuctionItemMapper auctionItemMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public Long createAuctionItem(CreateAuctionRequest request) {
        // 등록 요청에서 선택한 categoryId가 실제 존재하는지 확인한다.
        Category category = categoryMapper.findById(request.getItem().getCategoryId());

        if (category == null) {
            throw new CustomException(ErrorType.CATEGORY_NOT_FOUND);
        }

        // 현재 정책상 경매 등록은 최하위(level=3) 카테고리만 허용한다.
        if (!Integer.valueOf(3).equals(category.getLevel())) {
            throw new CustomException(ErrorType.INVALID_CATEGORY_LEVEL);
        }

        // request를 DB 저장용 auction_item 모델로 변환한다.
        AuctionItem auctionItem = AuctionItem.builder()
                .sellerId(request.getItem().getSellerId())
                .categoryId(request.getItem().getCategoryId())
                .brand(request.getItem().getBrand())
                .name(request.getItem().getName())
                .quality(request.getItem().getQuality())
                .description(request.getItem().getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        // auction_item 저장 후 생성된 PK를 모델에 주입받는다.
        int itemInsertCount = auctionItemMapper.insert(auctionItem);

        if (itemInsertCount != 1 || auctionItem.getId() == null) {
            throw new CustomException(ErrorType.AUCTION_ITEM_SAVE_FAILED);
        }

        return auctionItem.getId();
    }
}
