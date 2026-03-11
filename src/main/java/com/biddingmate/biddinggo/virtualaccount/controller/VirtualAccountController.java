package com.biddingmate.biddinggo.virtualaccount.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.virtualaccount.service.VirtualAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/virtual-accounts")
@RequiredArgsConstructor
public class VirtualAccountController {
    private final VirtualAccountService virtualAccountService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateVirtualAccountResponse>> createVirtualAccount(
            // @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody CreateVirtualAccountRequest request) {

        CreateVirtualAccountResponse result = virtualAccountService.createVirtualAccount(request);

        return ApiResponse.of(HttpStatus.OK, null, "가상계좌 발급 완료", result);
    }
}
