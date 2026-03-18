package com.biddingmate.biddinggo.auction.controller;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.auction.dto.CreateAuctionFromInspectionItemRequest;
import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.auction.dto.CreateAuctionResponse;
import com.biddingmate.biddinggo.auction.service.AuctionApplicationService;
import com.biddingmate.biddinggo.auction.service.AuctionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
@Tag(name = "Auction", description = "경매 등록 API")
public class AuctionController {
    private final AuctionApplicationService auctionApplicationService;
    private final AuctionQueryService auctionQueryService;

    @GetMapping("/{auctionId}")
    @Operation(summary = "경매 상세 조회", description = "경매 기본 정보, 상품 정보, 카테고리, 이미지 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<AuctionDetailResponse>> getAuctionDetail(
            @PathVariable Long auctionId) {

        AuctionDetailResponse result = auctionQueryService.getAuctionDetail(auctionId);

        return ApiResponse.of(HttpStatus.OK, null, "경매 상세 조회 완료", result);
    }

    @PostMapping("")
    @Operation(summary = "경매 등록", description = "경매 상품과 경매 정보를 함께 등록합니다.")
    public ResponseEntity<ApiResponse<CreateAuctionResponse>> createAuction(
            @Valid @RequestBody CreateAuctionRequest request) {

        Long auctionId = auctionApplicationService.createAuction(request);

        CreateAuctionResponse result = CreateAuctionResponse.builder()
                .auctionId(auctionId)
                .build();

        return ApiResponse.of(HttpStatus.OK, null, "경매 등록 완료", result);
    }

    @PostMapping("/inspection-items")
    @Operation(summary = "검수 완료 상품 경매 등록", description = "검수 완료된 기존 상품을 기준으로 경매를 등록합니다.")
    public ResponseEntity<ApiResponse<CreateAuctionResponse>> createAuctionFromInspectionItem(
            @Valid @RequestBody CreateAuctionFromInspectionItemRequest request) {

        Long auctionId = auctionApplicationService.createAuctionFromInspectionItem(request);

        CreateAuctionResponse result = CreateAuctionResponse.builder()
                .auctionId(auctionId)
                .build();

        return ApiResponse.of(HttpStatus.OK, null, "검수 완료 상품 경매 등록 완료", result);
    }
}
