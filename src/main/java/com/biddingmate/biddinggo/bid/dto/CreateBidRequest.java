package com.biddingmate.biddinggo.bid.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "입찰 등록 요청 DTO")
public class CreateBidRequest {
    private Long amount;
}