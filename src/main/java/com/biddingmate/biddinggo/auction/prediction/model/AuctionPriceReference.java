package com.biddingmate.biddinggo.auction.prediction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionPriceReference {
    private Long id;
    private Long auctionId;
    private Long itemId;
    private Long categoryId;
    private Long winnerPrice;
    private String quality;
    private Double conditionScore;
    private List<Double> embedding;
    private String embeddingModel;
    private Integer embeddingDimension;
    private String embeddingText;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
