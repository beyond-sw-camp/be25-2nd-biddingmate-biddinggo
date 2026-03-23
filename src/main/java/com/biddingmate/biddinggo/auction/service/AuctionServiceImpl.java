package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionFromInspectionItemRequest;
import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.auction.dto.UpdateAuctionRequest;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auction.model.AuctionStatus;
import com.biddingmate.biddinggo.auction.model.YesNo;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * auction 엔티티 생성만 담당하는 서비스 구현체.
 * 트랜잭션은 상위 애플리케이션 서비스에서 시작된 흐름에 참여한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionMapper auctionMapper;

    @Override
    @Transactional
    public void updateAuction(Long auctionId, UpdateAuctionRequest request) {
        validateUpdateRequest(auctionId, request);

        Auction auction = getAuctionForModification(auctionId);
        validateSeller(auction, request.getSellerId());

        if (!isAuctionUpdatableOrCancelable(auction)) {
            throw new CustomException(ErrorType.AUCTION_UPDATE_NOT_ALLOWED);
        }

        Auction updateTarget = Auction.builder()
                .id(auctionId)
                .startPrice(request.getStartPrice())
                .bidUnit(request.getBidUnit())
                .buyNowPrice(request.getBuyNowPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        int updatedCount = auctionMapper.updateAuction(updateTarget);

        if (updatedCount != 1) {
            throw new CustomException(ErrorType.AUCTION_UPDATE_NOT_ALLOWED);
        }
    }

    @Override
    @Transactional
    public void cancelAuction(Long auctionId, Long sellerId) {
        if (auctionId == null || auctionId <= 0 || sellerId == null || sellerId <= 0) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CANCEL_REQUEST);
        }

        Auction auction = getAuctionForModification(auctionId);
        validateSeller(auction, sellerId);

        if (!isAuctionUpdatableOrCancelable(auction)) {
            throw new CustomException(ErrorType.AUCTION_CANCEL_NOT_ALLOWED);
        }

        int updatedCount = auctionMapper.cancelAuction(auctionId, LocalDateTime.now(), AuctionStatus.CANCELLED);

        if (updatedCount != 1) {
            throw new CustomException(ErrorType.AUCTION_CANCEL_NOT_ALLOWED);
        }
    }

    @Override
    public Long createAuction(CreateAuctionRequest request, Long itemId) {
        if (itemId == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }

        // 일반 경매 등록은 검수 경매가 아니므로 inspectionYn은 서버에서 NO로 고정한다.
        Auction auction = Auction.builder()
                .itemId(itemId)
                .sellerId(request.getItem().getSellerId())
                .type(request.getAuction().getType())
                .inspectionYn(YesNo.NO)
                .startPrice(request.getAuction().getStartPrice())
                .bidUnit(request.getAuction().getBidUnit())
                .vickreyPrice(request.getAuction().getVickreyPrice())
                .buyNowPrice(request.getAuction().getBuyNowPrice())
                .startDate(request.getAuction().getStartDate())
                .endDate(request.getAuction().getEndDate())
                .createdAt(LocalDateTime.now())
                .build();

        // auction 저장 후 생성된 PK를 모델에 주입받는다.
        int auctionInsertCount = auctionMapper.insert(auction);

        if (auctionInsertCount != 1 || auction.getId() == null) {
            throw new CustomException(ErrorType.AUCTION_SAVE_FAILED);
        }

        return auction.getId();
    }

    @Override
    public Long createAuction(CreateAuctionFromInspectionItemRequest request) {
        if (request.getItemId() == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }

        // 검수 완료 상품 기반 경매 등록은 inspectionYn을 서버에서 YES로 고정한다.
        Auction auction = Auction.builder()
                .itemId(request.getItemId())
                .sellerId(request.getSellerId())
                .type(request.getAuction().getType())
                .inspectionYn(YesNo.YES)
                .startPrice(request.getAuction().getStartPrice())
                .bidUnit(request.getAuction().getBidUnit())
                .vickreyPrice(request.getAuction().getVickreyPrice())
                .buyNowPrice(request.getAuction().getBuyNowPrice())
                .startDate(request.getAuction().getStartDate())
                .endDate(request.getAuction().getEndDate())
                .createdAt(LocalDateTime.now())
                .build();

        int auctionInsertCount = auctionMapper.insert(auction);

        if (auctionInsertCount != 1 || auction.getId() == null) {
            throw new CustomException(ErrorType.AUCTION_SAVE_FAILED);
        }

        return auction.getId();
    }

    private void validateUpdateRequest(Long auctionId, UpdateAuctionRequest request) {
        if (auctionId == null || auctionId <= 0 || request == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_UPDATE_REQUEST);
        }

        if (request.getSellerId() == null || request.getSellerId() <= 0) {
            throw new CustomException(ErrorType.INVALID_AUCTION_UPDATE_REQUEST);
        }

        if (request.getStartPrice() == null || request.getBidUnit() == null
                || request.getStartDate() == null || request.getEndDate() == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_UPDATE_REQUEST);
        }
    }

    private Auction getAuctionForModification(Long auctionId) {
        Auction auction = auctionMapper.findByIdForUpdate(auctionId);

        if (auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        return auction;
    }

    private void validateSeller(Auction auction, Long sellerId) {
        if (!auction.getSellerId().equals(sellerId)) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }
    }

    private boolean isAuctionUpdatableOrCancelable(Auction auction) {
        if (auction.getStatus() == AuctionStatus.PENDING) {
            return true;
        }

        return auction.getStatus() == AuctionStatus.ON_GOING
                && auction.getBidCount() != null
                && auction.getBidCount() == 0;
    }
}
