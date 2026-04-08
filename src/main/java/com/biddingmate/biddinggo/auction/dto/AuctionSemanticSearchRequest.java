package com.biddingmate.biddinggo.auction.dto;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "유사도 기반 경매 검색 요청 DTO")
public class AuctionSemanticSearchRequest extends BasePageRequest {
    @NotBlank(message = "검색어는 필수입니다.")
    @Schema(description = "검색어", example = "나이키 조던 검정 하이탑")
    private String q;
}
