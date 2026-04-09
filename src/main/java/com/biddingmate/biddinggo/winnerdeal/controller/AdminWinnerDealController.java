package com.biddingmate.biddinggo.winnerdeal.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.winnerdeal.dto.AdminWinnerDealListRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.AdminWinnerDealListResponse;
import com.biddingmate.biddinggo.winnerdeal.service.WinnerDealQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/winner-deals")
@Tag(name = "Admin-Winner-Deal", description = "관리자 거래 관리 API")
public class AdminWinnerDealController {
    private final WinnerDealQueryService winnerDealQueryService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "관리자 거래 내역 조회", description = "관리자가 거래 번호와 상태 조건으로 거래 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<AdminWinnerDealListResponse>>> findAdminWinnerDealHistory(@Valid AdminWinnerDealListRequest request) {
        PageResponse<AdminWinnerDealListResponse> result = winnerDealQueryService.findAdminWinnerDealHistory(request);

        return ApiResponse.of(HttpStatus.OK, null, "관리자 거래 내역 조회에 성공했습니다.", result);
    }
}
