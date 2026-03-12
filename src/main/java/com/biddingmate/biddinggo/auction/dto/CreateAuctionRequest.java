package com.biddingmate.biddinggo.auction.dto;

import com.biddingmate.biddinggo.auction.model.AuctionType;
import com.biddingmate.biddinggo.auction.model.YesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuctionRequest {
    private Long sellerId;
    private Long categoryId;
    private String brand;
    private String name;
    private String quality;
    private String description;
    private AuctionType type;
    private YesNo inspectionYn;
    private Long startPrice;
    private Integer bidUnit;
    private Long vickreyPrice;
    private Long buyNowPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
