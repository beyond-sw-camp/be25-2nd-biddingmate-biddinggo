package com.biddingmate.biddinggo.virtualaccount.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.virtualaccount.dto.GetVirtualAccountResponse;
import com.biddingmate.biddinggo.virtualaccount.service.VirtualAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

        return ApiResponse.of(HttpStatus.OK, null, "가상계좌 발급 성공", result);
    }

    @GetMapping("")
//    public ResponseEntity<ApiResponse<List<GetVirtualAccountResponse>>> getVirtualAccount(@AuthenticationPrincipal PrincipalDetails principal) {
//        Member member = principal.getMember();
//        Long memberId = member.getId();
    public ResponseEntity<ApiResponse<List<GetVirtualAccountResponse>>> getVirtualAccount(@RequestParam Long memberId) {
        List<GetVirtualAccountResponse> result = virtualAccountService.getVirtualAccount(memberId);



        return ApiResponse.of(HttpStatus.OK, null, "가삭계좌 조회 성공", result);
    }
}
