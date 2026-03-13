package com.biddingmate.biddinggo.auction.controller;

import com.biddingmate.biddinggo.auction.dto.CreateAuctionRequest;
import com.biddingmate.biddinggo.auction.dto.CreateAuctionResponse;
import com.biddingmate.biddinggo.auction.service.AuctionApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("")
    @Operation(summary = "경매 등록", description = "경매 상품과 경매 정보를 함께 등록합니다.")
    public ResponseEntity<ApiResponse<CreateAuctionResponse>> createAuction(
            @RequestBody CreateAuctionRequest request) {

        Long auctionId = auctionApplicationService.createAuction(request);

        CreateAuctionResponse result = CreateAuctionResponse.builder()
                .auctionId(auctionId)
                .build();

        return ApiResponse.of(HttpStatus.OK, null, "경매 등록 완료", result);
    }
}
