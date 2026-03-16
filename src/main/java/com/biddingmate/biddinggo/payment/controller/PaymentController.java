package com.biddingmate.biddinggo.payment.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.payment.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.payment.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.payment.dto.GetVirtualAccountResponse;
import com.biddingmate.biddinggo.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/virtual-accounts")
    public ResponseEntity<ApiResponse<CreateVirtualAccountResponse>> createVirtualAccount(
            // @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody CreateVirtualAccountRequest request) {

        CreateVirtualAccountResponse result = paymentService.createVirtualAccount(request);

        return ApiResponse.of(HttpStatus.OK, null, "가상계좌 발급 성공", result);
    }

    @GetMapping("virtual-accounts")
//    public ResponseEntity<ApiResponse<List<GetVirtualAccountResponse>>> getVirtualAccount(@AuthenticationPrincipal PrincipalDetails principal) {
//        Member member = principal.getMember();
//        Long memberId = member.getId();
    public ResponseEntity<ApiResponse<List<GetVirtualAccountResponse>>> getVirtualAccount(@RequestParam Long memberId) {
        List<GetVirtualAccountResponse> result = paymentService.getVirtualAccount(memberId);



        return ApiResponse.of(HttpStatus.OK, null, "가상계좌 조회 성공", result);
    }
}
