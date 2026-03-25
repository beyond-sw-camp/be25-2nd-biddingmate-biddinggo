package com.biddingmate.biddinggo.wishlist.controller;

import com.biddingmate.biddinggo.auction.dto.AuctionDetailResponse;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistRequest;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistResponse;
import com.biddingmate.biddinggo.wishlist.dto.WishlistCountResponse;
import com.biddingmate.biddinggo.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.annotations.NotNull;

@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "관심 경매 API")
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping("")
    @Operation(summary = "관심 경매 등록", description = "관심 경매를 등록합니다.")
    public ResponseEntity<ApiResponse<CreateWishlistResponse>> createWishlist(
            @RequestBody CreateWishlistRequest request,
            /*
              <<to-do>>
              이후 인증 구현 완료 후, 로그인 정보 받아와서 memberId에 주입
             */
            @NotNull @RequestParam Long memberId
    ) {

        CreateWishlistResponse result = wishlistService.createWishlist(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "관심 경매 등록 성공", result);
    }

    @GetMapping("/count")
    @Operation(summary = "경매 관심 수 조회", description = "경매의 관심 수를 조회합니다.")
    public ResponseEntity<ApiResponse<WishlistCountResponse>> getWishlistCount(
            @NotNull @RequestParam Long auctionId
    ){
        WishlistCountResponse result = wishlistService.getWishlistCount(auctionId);

        return ApiResponse.of(HttpStatus.OK, null, "경매 관심 수 조회 성공", result);
    }

    @GetMapping("")
    @Operation(summary = "내 관심 경매 조회", description = "사용자의 관심 경매를 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<AuctionDetailResponse>>> getWishlist(
            BasePageRequest request,
            /*
              <<to-do>>
              이후 인증 구현 완료 후, 로그인 정보 받아와서 memberId에 주입
             */
            @NotNull @RequestParam Long memberId
    ) {
        PageResponse<AuctionDetailResponse> result = wishlistService.findWishlistAuctionsByMemberId(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "관심 경매 조회 성공", result);
    }

    @DeleteMapping("/")
    @Operation(summary = "관심 경매 삭제", description = "관심 경매를 삭제합니다.")
    public ResponseEntity<ApiResponse<Integer>> deleteWishlist(
            @RequestBody CreateWishlistRequest request,
            /*
              <<to-do>>
              이후 인증 구현 완료 후, 로그인 정보 받아와서 memberId에 주입
             */
            @NotNull @RequestParam Long memberId
    ) {

        int result = wishlistService.deleteWishlist(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "관심 경매 삭제 성공", result);
    }
}
