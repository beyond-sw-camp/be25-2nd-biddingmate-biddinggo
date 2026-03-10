package com.biddingmate.biddinggo.point.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping("/virtual-account")
    public ResponseEntity<ApiResponse<CreateVirtualAccountResponse>> createVirtualAccount(
            // @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody CreateVirtualAccountRequest request) {

        CreateVirtualAccountResponse result = pointService.createVirtualAccount(request);

        return ApiResponse.of(HttpStatus.OK, null, "가상계좌 발급 완료", result);
    }
}
