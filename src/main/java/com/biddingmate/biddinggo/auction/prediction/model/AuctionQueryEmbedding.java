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
public class AuctionQueryEmbedding {
    private Long id;
    private Long auctionId;
    private Long itemId;
    private Long categoryId;
    private List<Double> embedding;
    private String embeddingModel;
    private Integer embeddingDimension;
    private String embeddingText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
