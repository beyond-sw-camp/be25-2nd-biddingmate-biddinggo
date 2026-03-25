package com.biddingmate.biddinggo.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "경매 취소 요청 DTO")
public class CancelAuctionRequest {
    @Schema(description = "판매자 ID", example = "1")
    @NotNull(message = "판매자 ID는 필수입니다.")
    private Long sellerId;
}
