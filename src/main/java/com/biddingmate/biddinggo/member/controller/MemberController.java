package com.biddingmate.biddinggo.member.controller;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileUpdateRequest;
import com.biddingmate.biddinggo.member.dto.MemberPurchaseItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberSalesItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberSellerProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberSellingItemResponse;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberDashboardResponse>> getDashboard(@AuthenticationPrincipal Member member) {
        MemberDashboardResponse result = memberService.getMyDashboard(member.getId());
        return ApiResponse.of(HttpStatus.OK, null, "회원 마이페이지 조회 성공", result);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> getProfile(@AuthenticationPrincipal Member member) {
        MemberProfileResponse result = memberService.getMyProfile(member.getId());
        return ApiResponse.of(HttpStatus.OK, null, "회원 프로필 조회 성공", result);
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal Member member,
            @RequestBody MemberProfileUpdateRequest request
    ) {
        MemberProfileResponse result = memberService.updateMyProfile(member.getId(), request);
        return ApiResponse.of(HttpStatus.OK, null, "회원 프로필 수정 성공", result);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@AuthenticationPrincipal Member member) {
        memberService.deleteMyAccount(member.getId());
        return ApiResponse.of(HttpStatus.OK, null, "회원 탈퇴 성공", null);
    }

    @GetMapping("/me/sales")
    public ResponseEntity<ApiResponse<PageResponse<MemberSalesItemResponse>>> getSales(
            @AuthenticationPrincipal Member member,
            @Valid BasePageRequest pageRequest
    ) {
        PageResponse<MemberSalesItemResponse> result = memberService.getMySales(member.getId(), pageRequest);
        return ApiResponse.of(HttpStatus.OK, null, "판매 내역 조회 성공", result);
    }

    @GetMapping("/me/purchases")
    public ResponseEntity<ApiResponse<PageResponse<MemberPurchaseItemResponse>>> getPurchases(
            @AuthenticationPrincipal Member member,
            @Valid BasePageRequest pageRequest
    ) {
        PageResponse<MemberPurchaseItemResponse> result = memberService.getMyPurchases(member.getId(), pageRequest);
        return ApiResponse.of(HttpStatus.OK, null, "구매 내역 조회 성공", result);
    }

    @GetMapping("/me/auctions")
    public ResponseEntity<ApiResponse<PageResponse<MemberSellingItemResponse>>> getSellingItems(
            @AuthenticationPrincipal Member member,
            @RequestParam String status,
            @Valid BasePageRequest pageRequest
    ) {
        PageResponse<MemberSellingItemResponse> result =
                memberService.getMySellingItems(member.getId(), status, pageRequest);

        return ApiResponse.of(HttpStatus.OK, null, "판매 중인 상품 조회 성공", result);
    }
    @GetMapping("/{sellerId}")
    public ResponseEntity<ApiResponse<MemberSellerProfileResponse>> getSellerProfile(
            @PathVariable Long sellerId
    ) {
        MemberSellerProfileResponse result = memberService.getSellerProfile(sellerId);
        return ApiResponse.of(HttpStatus.OK, null, "판매자 정보 조회 성공", result);
    }
}