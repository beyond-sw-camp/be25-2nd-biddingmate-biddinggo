package com.biddingmate.biddinggo.winnerdeal.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryRequest;
import com.biddingmate.biddinggo.winnerdeal.dto.WinnerDealHistoryResponse;
import com.biddingmate.biddinggo.winnerdeal.service.WinnerDealQueryService;
import com.biddingmate.biddinggo.winnerdeal.service.WinnerDealService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/winner-deals")
public class WinnerDealController {
    private final WinnerDealService winnerDealService;
    private final WinnerDealQueryService winnerDealQueryService;

    @GetMapping("/purchases")
    public ResponseEntity<ApiResponse<PageResponse<WinnerDealHistoryResponse>>> findPurchaseHistory(
            @Valid WinnerDealHistoryRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Member member
    ) {
        PageResponse<WinnerDealHistoryResponse> result =
                winnerDealQueryService.findPurchaseHistory(request, member.getId());

        return ApiResponse.of(HttpStatus.OK, null, "구매 내역 조회에 성공했습니다.", result);
    }

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<PageResponse<WinnerDealHistoryResponse>>> findSaleHistory(
            @Valid WinnerDealHistoryRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Member member
    ) {
        PageResponse<WinnerDealHistoryResponse> result =
                winnerDealQueryService.findSaleHistory(request, member.getId());

        return ApiResponse.of(HttpStatus.OK, null, "판매 내역 조회에 성공했습니다.", result);
    }
}
