package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.auction.mapper.AuctionMybatisMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * auction 엔티티 생성만 담당하는 서비스 구현체.
 * 트랜잭션은 상위 애플리케이션 서비스에서 시작된 흐름에 참여한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionMybatisMapper auctionMybatisMapper;

    @Override
    public Long createAuction(CreateAuctionRequest request, Long itemId) {
        if (request == null
                || itemId == null
                || request.getSellerId() == null
                || request.getStartDate() == null
                || request.getEndDate() == null
                || !request.getEndDate().isAfter(request.getStartDate())) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }

        // request를 DB 저장용 모델로 변환한다.
        Auction auction = Auction.builder()
                .itemId(itemId)
                .sellerId(request.getSellerId())
                .type(request.getType())
                .inspectionYn(request.getInspectionYn())
                .startPrice(request.getStartPrice())
                .bidUnit(request.getBidUnit())
                .vickreyPrice(request.getVickreyPrice())
                .buyNowPrice(request.getBuyNowPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDateTime.now())
                .build();

        // auction 저장 후 생성된 PK를 모델에 주입받는다.
        int auctionInsertCount = auctionMybatisMapper.insert(auction);

        if (auctionInsertCount != 1 || auction.getId() == null) {
            throw new CustomException(ErrorType.AUCTION_SAVE_FAILED);
        }

        return auction.getId();
    }
}
