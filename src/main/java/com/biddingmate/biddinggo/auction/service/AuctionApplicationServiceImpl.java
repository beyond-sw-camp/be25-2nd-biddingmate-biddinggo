package com.biddingmate.biddinggo.auction.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.file.service.FileService;
import com.biddingmate.biddinggo.item.service.AuctionItemService;
import com.biddingmate.biddinggo.item.service.ItemImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 경매 등록 전체 흐름을 조율하는 애플리케이션 서비스.
 * 트랜잭션 경계는 이 클래스에서만 관리한다.
 */
@Service
@RequiredArgsConstructor
public class AuctionApplicationServiceImpl implements AuctionApplicationService {
    private final AuctionItemService auctionItemService;
    private final ItemImageService itemImageService;
    private final AuctionService auctionService;
    private final FileService fileService;

    @Override
    @Transactional
    public Long createAuction(CreateAuctionRequest request) {
        List<String> uploadedFileKeys = extractUploadedFileKeys(request);

        try {
            validateRequest(request);

            // 1. auction_item 먼저 생성하여 itemId를 확보한다.
            Long itemId = auctionItemService.createAuctionItem(request);

            // 2. 업로드된 이미지 메타데이터를 item_image에 저장한다.
            itemImageService.createItemImages(itemId, request.getItem().getImages());

            // 3. 생성된 itemId로 auction을 생성한다.
            return auctionService.createAuction(request, itemId);
        } catch (RuntimeException exception) {
            fileService.deleteFiles(uploadedFileKeys);
            throw exception;
        }
    }

    /**
     * DTO 기본 검증은 컨트롤러에서 처리하고,
     * 여기서는 비즈니스 규칙만 검증한다.
     */
    private void validateRequest(CreateAuctionRequest request) {
        if (!request.getAuction().getEndDate().isAfter(request.getAuction().getStartDate())) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }
    }

    private List<String> extractUploadedFileKeys(CreateAuctionRequest request) {
        if (request == null || request.getItem() == null || request.getItem().getImages() == null) {
            return List.of();
        }

        return request.getItem().getImages().stream()
                .filter(image -> image != null && image.getFileKey() != null && !image.getFileKey().isBlank())
                .map(CreateAuctionRequest.Image::getFileKey)
                .toList();
    }
}
