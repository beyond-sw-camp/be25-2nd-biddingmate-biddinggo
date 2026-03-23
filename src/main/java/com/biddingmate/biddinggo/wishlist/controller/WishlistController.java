package com.biddingmate.biddinggo.wishlist.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.wishlist.dto.CreateWishlistResponse;
import com.biddingmate.biddinggo.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/{auctionId}")
    @Operation(summary = "관심 경매 등록", description = "관심 경매를 등록합니다.")
    public ResponseEntity<ApiResponse<CreateWishlistResponse>> createWishlist(
            @NotNull @PathVariable Long auctionId,
            /*
              <<to-do>>
              이후 인증 구현 완료 후, 로그인 정보 받아와서 memberId에 주입
             */
            @NotNull @RequestParam Long memberId
    ) {

        CreateWishlistResponse result = wishlistService.createWishlist(memberId, auctionId);

        return ApiResponse.of(HttpStatus.OK, null, "관심 경매 등록 성공", result);
    }
}
