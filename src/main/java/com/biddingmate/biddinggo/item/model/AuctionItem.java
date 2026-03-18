package com.biddingmate.biddinggo.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionItem {
    private Long id;
    private Long sellerId;
    private Long categoryId;
    private String brand;
    private String name;
    private String quality;
    private String description;
    private AuctionItemStatus status;
    private ItemInspectionStatus inspectionStatus;
    private LocalDateTime returnedAt;
    private LocalDateTime createdAt;
}
