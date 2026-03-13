package com.biddingmate.biddinggo.item.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.AuctionItemMybatisMapper;
import com.biddingmate.biddinggo.item.model.AuctionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * auction_item 엔티티 생성만 담당하는 서비스 구현체.
 */
@Service
@RequiredArgsConstructor
public class AuctionItemServiceImpl implements AuctionItemService {
    private final AuctionItemMybatisMapper auctionItemMybatisMapper;

    @Override
    public Long createAuctionItem(CreateAuctionRequest request) {
        if (request == null
                || request.getItem() == null
                || request.getItem().getSellerId() == null
                || request.getItem().getCategoryId() == null
                || request.getItem().getName() == null || request.getItem().getName().isBlank()) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
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
        int itemInsertCount = auctionItemMybatisMapper.insert(auctionItem);

        if (itemInsertCount != 1 || auctionItem.getId() == null) {
            throw new CustomException(ErrorType.AUCTION_ITEM_SAVE_FAILED);
        }

        return auctionItem.getId();
    }
}
