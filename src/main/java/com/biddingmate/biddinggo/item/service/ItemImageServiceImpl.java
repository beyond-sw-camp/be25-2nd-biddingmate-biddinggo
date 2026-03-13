package com.biddingmate.biddinggo.item.service;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.item.mapper.ItemImageMybatisMapper;
import com.biddingmate.biddinggo.item.model.ItemImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * item_image 엔티티 생성만 담당하는 서비스 구현체.
 */
@Service
@RequiredArgsConstructor
public class ItemImageServiceImpl implements ItemImageService {
    private final ItemImageMybatisMapper itemImageMybatisMapper;

    @Override
    public void createItemImages(Long itemId, List<CreateAuctionRequest.Image> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        if (itemId == null) {
            throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
        }

        for (CreateAuctionRequest.Image image : images) {
            if (image == null
                    || image.getUrl() == null || image.getUrl().isBlank()
                    || image.getDisplayOrder() == null || image.getDisplayOrder() <= 0
                    || image.getType() == null || image.getType().isBlank()
                    || image.getSize() == null || image.getSize() <= 0) {
                throw new CustomException(ErrorType.INVALID_AUCTION_CREATE_REQUEST);
            }

            ItemImage itemImage = ItemImage.builder()
                    .itemId(itemId)
                    .url(image.getUrl())
                    .displayOrder(image.getDisplayOrder())
                    .type(image.getType())
                    .size(image.getSize())
                    .createdAt(LocalDateTime.now())
                    .build();

            int imageInsertCount = itemImageMybatisMapper.insert(itemImage);

            if (imageInsertCount != 1 || itemImage.getId() == null) {
                throw new CustomException(ErrorType.ITEM_IMAGE_SAVE_FAILED);
            }
        }
    }
}
