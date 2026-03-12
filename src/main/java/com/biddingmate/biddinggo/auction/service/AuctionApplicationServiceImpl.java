package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.service.AuctionItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 경매 등록 전체 흐름을 조율하는 애플리케이션 서비스.
 * 트랜잭션 경계는 이 클래스에서만 관리한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionApplicationServiceImpl implements AuctionApplicationService {
    private final AuctionItemService auctionItemService;
    private final AuctionService auctionService;

    @Override
    @Transactional
    public Long createAuction(CreateAuctionRequest request) {
        validateRequest(request);

        // 1. auction_item 먼저 생성하여 itemId를 확보한다.
        Long itemId = auctionItemService.createAuctionItem(request);

        // 2. 생성된 itemId로 auction을 생성한다.
        return auctionService.createAuction(request, itemId);
    }

    /**
     * 두 하위 서비스가 공통으로 사용하는 필수 입력값을 선검증한다.
     */
    private void validateRequest(CreateAuctionRequest request) {
        if (request == null
                || request.getSellerId() == null
                || request.getCategoryId() == null
                || request.getName() == null || request.getName().isBlank()
                || request.getStartDate() == null
                || request.getEndDate() == null
                || !request.getEndDate().isAfter(request.getStartDate())) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }
    }
}
