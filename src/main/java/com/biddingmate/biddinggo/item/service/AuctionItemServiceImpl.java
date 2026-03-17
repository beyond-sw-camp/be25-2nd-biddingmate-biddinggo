package com.biddingmate.biddinggo.item.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.dto.AuctionItemCreateSource;
import com.biddingmate.biddinggo.item.mapper.AuctionItemMapper;
import com.biddingmate.biddinggo.item.mapper.CategoryMapper;
import com.biddingmate.biddinggo.item.model.AuctionItem;
import com.biddingmate.biddinggo.item.model.AuctionItemStatus;
import com.biddingmate.biddinggo.item.model.Category;
import com.biddingmate.biddinggo.item.model.InspectionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * auction_item 엔티티 생성만 담당하는 서비스 구현체.
 * 경매 등록 시 선택한 카테고리가 실제 최하위 카테고리인지 함께 검증한다.
 * {@code auction_item.status}, {@code auction_item.inspection_status} 결정도 이 클래스가 담당한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionItemServiceImpl implements AuctionItemService {
    private final AuctionItemMapper auctionItemMapper;
    private final CategoryMapper categoryMapper;

    @Override
    /**
     * 경매 등록용 item 생성.
     * status/inspectionStatus를 null로 넘겨 DB 기본값을 사용한다.
     */
    public Long createAuctionItem(AuctionItemCreateSource item) {
        return createItem(item, null, null);
    }

    @Override
    /**
     * 검수 등록용 item 생성.
     * 검수 대기 상품이므로 status와 inspectionStatus를 모두 PENDING으로 명시한다.
     */
    public Long createInspectionItem(AuctionItemCreateSource item) {
        return createItem(item, AuctionItemStatus.PENDING, InspectionStatus.PENDING);
    }

    /**
     * 공통 auction_item 저장 로직.
     *
     * <p>이 메서드는 상품 기본 정보 저장뿐 아니라
     * {@code auction_item.status}, {@code auction_item.inspection_status} 초기값도 함께 결정한다.</p>
     * <p>두 값이 null이면 mapper에서 해당 컬럼을 INSERT에서 제외하고 DB 기본값을 사용한다.</p>
     */
    private Long createItem(AuctionItemCreateSource item, AuctionItemStatus status, InspectionStatus inspectionStatus) {
        // 등록 요청에서 선택한 categoryId가 실제 존재하는지 확인한다.
        Category category = categoryMapper.findById(item.getCategoryId());

        if (category == null) {
            throw new CustomException(ErrorType.CATEGORY_NOT_FOUND);
        }

        // 현재 정책상 경매 등록은 최하위(level=3) 카테고리만 허용한다.
        if (!Integer.valueOf(3).equals(category.getLevel())) {
            throw new CustomException(ErrorType.INVALID_CATEGORY_LEVEL);
        }

        // request를 DB 저장용 auction_item 모델로 변환한다.
        // inspection_status는 AuctionService/InspectionService가 아니라 여기서 세팅된다.
        AuctionItem auctionItem = AuctionItem.builder()
                .sellerId(item.getSellerId())
                .categoryId(item.getCategoryId())
                .brand(item.getBrand())
                .name(item.getName())
                .quality(item.getQuality())
                .description(item.getDescription())
                .status(status)
                .inspectionStatus(inspectionStatus)
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
