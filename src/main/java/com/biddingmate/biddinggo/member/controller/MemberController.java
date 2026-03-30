package com.biddingmate.biddinggo.member.controller;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileUpdateRequest;
import com.biddingmate.biddinggo.member.dto.MemberPurchaseItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberSalesItemResponse;
import com.biddingmate.biddinggo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<ApiResponse<MemberDashboardResponse>> getDashboard(@RequestParam Long memberId) {
        MemberDashboardResponse result = memberService.getMyDashboard(memberId);
        return ApiResponse.of(HttpStatus.OK, null, "회원 마이페이지 조회 성공", result);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> getProfile(@RequestParam Long memberId) {
        MemberProfileResponse result = memberService.getMyProfile(memberId);
        return ApiResponse.of(HttpStatus.OK, null, "회원 프로필 조회 성공", result);
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> updateMyProfile(
            @RequestParam Long memberId,
            @RequestBody MemberProfileUpdateRequest request
    ) {
        MemberProfileResponse result = memberService.updateMyProfile(memberId, request);
        return ApiResponse.of(HttpStatus.OK, null, "회원 프로필 수정 성공", result);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@RequestParam Long memberId) {
        memberService.deleteMyAccount(memberId);
        return ApiResponse.of(HttpStatus.OK, null, "회원 탈퇴 성공", null);
    }

    @GetMapping("/me/sales")
    public ResponseEntity<ApiResponse<PageResponse<MemberSalesItemResponse>>> getSales(
            @RequestParam Long memberId,
            @Valid BasePageRequest pageRequest
    ) {
        PageResponse<MemberSalesItemResponse> result = memberService.getMySales(memberId, pageRequest);
        return ApiResponse.of(HttpStatus.OK, null, "판매 내역 조회 성공", result);
    }

    @GetMapping("/me/purchases")
    public ResponseEntity<ApiResponse<PageResponse<MemberPurchaseItemResponse>>> getPurchases(
            @RequestParam Long memberId,
            @Valid BasePageRequest pageRequest
    ) {
        PageResponse<MemberPurchaseItemResponse> result = memberService.getMyPurchases(memberId, pageRequest);
        return ApiResponse.of(HttpStatus.OK, null, "구매 내역 조회 성공", result);
    }
}