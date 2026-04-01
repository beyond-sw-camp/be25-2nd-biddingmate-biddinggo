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
    /**
     * 경매 수정 메인 로직.
     * 판매자 본인 여부와 수정 가능 상태를 확인한 뒤 auction 테이블의 수정 가능 필드만 반영한다.
     */
    public void updateAuction(Long auctionId, UpdateAuctionRequest request, Long sellerId) {
        validateUpdateRequest(auctionId, request, sellerId);

        // 동시 수정 충돌을 줄이기 위해 수정 대상 경매를 lock 조회한다.
        Auction auction = getAuctionForModification(auctionId);
        validateSeller(auction, sellerId);

        // 정책상 PENDING 또는 ON_GOING + bidCount == 0 인 경우에만 수정 가능하다.
        if (!isAuctionUpdatableOrCancelable(auction)) {
            throw new CustomException(ErrorType.AUCTION_UPDATE_NOT_ALLOWED);
        }

        // 현재 정책상 경매 메타 정보(가격/일정)만 수정 대상으로 둔다.
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
    /**
     * 경매 취소 메인 로직.
     * 판매자 본인 여부와 취소 가능 상태를 확인한 뒤 status와 cancel_date를 함께 갱신한다.
     */
    public void cancelAuction(Long auctionId, Long sellerId) {
        if (auctionId == null || auctionId <= 0 || sellerId == null || sellerId <= 0) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CANCEL_REQUEST);
        }

        // 취소 직전 상태를 정확히 확인하기 위해 lock 조회한다.
        Auction auction = getAuctionForModification(auctionId);
        validateSeller(auction, sellerId);

        // 수정과 동일한 정책으로 취소 가능 여부를 판단한다.
        if (!isAuctionUpdatableOrCancelable(auction)) {
            throw new CustomException(ErrorType.AUCTION_CANCEL_NOT_ALLOWED);
        }

        int updatedCount = auctionMapper.cancelAuction(auctionId, LocalDateTime.now(), AuctionStatus.CANCELLED);

        if (updatedCount != 1) {
            throw new CustomException(ErrorType.AUCTION_CANCEL_NOT_ALLOWED);
        }
    }

    @Override
    public Long createAuction(CreateAuctionRequest request, Long itemId, Long sellerId) {
        if (itemId == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }

        // 일반 경매 등록은 검수 경매가 아니므로 inspectionYn은 서버에서 NO로 고정한다.
        Auction auction = Auction.builder()
                .itemId(itemId)
                .sellerId(sellerId)
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
    public Long createAuction(CreateAuctionFromInspectionItemRequest request, Long sellerId) {
        if (request.getItemId() == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }

        // 검수 완료 상품 기반 경매 등록은 inspectionYn을 서버에서 YES로 고정한다.
        Auction auction = Auction.builder()
                .itemId(request.getItemId())
                .sellerId(sellerId)
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

    /**
     * 수정 요청 기본값을 검증한다.
     * 경매 ID, 판매자 ID, 수정 필수 필드가 비어 있으면 요청 오류로 처리한다.
     */
    private void validateUpdateRequest(Long auctionId, UpdateAuctionRequest request, Long sellerId) {
        if (auctionId == null || auctionId <= 0 || request == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_UPDATE_REQUEST);
        }

        if (sellerId == null || sellerId <= 0) {
            throw new CustomException(ErrorType.INVALID_AUCTION_UPDATE_REQUEST);
        }

        if (request.getStartPrice() == null || request.getBidUnit() == null
                || request.getStartDate() == null || request.getEndDate() == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_UPDATE_REQUEST);
        }
    }

    /**
     * 수정/취소 대상 경매를 배타적으로 조회한다.
     */
    private Auction getAuctionForModification(Long auctionId) {
        Auction auction = auctionMapper.findByIdForUpdate(auctionId);

        if (auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        return auction;
    }

    /**
     * 요청 판매자와 실제 경매 판매자가 같은지 검증한다.
     */
    private void validateSeller(Auction auction, Long sellerId) {
        if (!auction.getSellerId().equals(sellerId)) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }
    }

    /**
     * 경매 수정/취소 가능 여부를 판단한다.
     * PENDING 은 항상 허용하고, ON_GOING 은 입찰 수가 0일 때만 허용한다.
     */
    private boolean isAuctionUpdatableOrCancelable(Auction auction) {
        if (auction.getStatus() == AuctionStatus.PENDING) {
            return true;
        }

        return auction.getStatus() == AuctionStatus.ON_GOING
                && auction.getBidCount() != null
                && auction.getBidCount() == 0;
    }
}
