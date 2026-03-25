package com.biddingmate.biddinggo.bid.controller;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.bid.dto.BidResponse;
import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.dto.CreateBidResponse;
import com.biddingmate.biddinggo.bid.service.BidApplicationService;
import com.biddingmate.biddinggo.bid.service.BidService;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.annotations.NotNull;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Tag(name = "Bid", description = "입찰 API")
public class BidController {
    private final BidApplicationService bidApplicationService;
    private final BidService bidService;

    @PostMapping("/bids")
    @Operation(summary = "입찰", description = "경매에 입찰합니다.")
    public ResponseEntity<ApiResponse<CreateBidResponse>> createBid(
            /*
              <<to-do>>
              이후 인증 구현 완료 후, 로그인 정보 받아와서 memberId에 주입
             */
            @NotNull @RequestParam Long memberId,
            @Valid @RequestBody CreateBidRequest request
    ) {

        CreateBidResponse result = bidApplicationService.createBidProcess(memberId, request);

        return ApiResponse.of(HttpStatus.OK, null, "입찰 완료", result);
    }

    @GetMapping("/bids")
    @Operation(summary = "입찰 기록 조회", description = "경매에 대한 입찰 기록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<BidResponse>>> getBidsByAuctionId(
            BasePageRequest request,
            @NotNull @RequestParam("auctionId") Long auctionId
    ) {
        PageResponse<BidResponse> result = bidService.getBidsByAuctionId(request, auctionId);

        return ApiResponse.of(HttpStatus.OK, null, "입찰 기록 조회 성공", result);
    }

    @GetMapping("/users/me/bids")
    @Operation(summary = "입찰 중인 경매 조회", description = "입찰 중인 경매를 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<AuctionDetailResponse>>> getBidAuctionsForUser(
        BasePageRequest request,
            /*
              <<to-do>>
              이후 인증 구현 완료 후, 로그인 정보 받아와서 memberId에 주입
             */
        @NotNull @RequestParam Long memberId
    ) {
        PageResponse<AuctionDetailResponse> result = bidService.getBidAuctionsByMemberId(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "입찰 중인 경매 조회 성공", result);
    }
}
