package com.biddingmate.biddinggo.payment.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.payment.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.payment.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.payment.dto.GetVirtualAccountResponse;
import com.biddingmate.biddinggo.payment.dto.TossDepositWebhook;
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

    @GetMapping("/virtual-accounts")
// 인증 구현 전이므로 인증 Principal을 담을 수 없기 때문에 @RequestParam으로 memberId 대체
//    public ResponseEntity<ApiResponse<List<GetVirtualAccountResponse>>> getVirtualAccount(@AuthenticationPrincipal PrincipalDetails principal) {
//        Member member = principal.getMember();
//        Long memberId = member.getId();
    public ResponseEntity<ApiResponse<List<GetVirtualAccountResponse>>> getVirtualAccount(@RequestParam Long memberId) {
        List<GetVirtualAccountResponse> result = paymentService.getVirtualAccount(memberId);

        return ApiResponse.of(HttpStatus.OK, null, "가상계좌 조회 성공", result);
    }

    @PostMapping("/virtual-accounts/deposit")
    public ResponseEntity<ApiResponse<Void>> handleTossDepositWebhook(@RequestBody TossDepositWebhook request) {
        // 1. 로그로 데이터 확인 (가장 중요!)
        System.out.println("토스 웹훅 도착!: \n" + request);

        // 2. 서비스 로직 호출 (입금 완료 처리 등)
        paymentService.processDeposit(request);

        // 3. 토스에게 성공적으로 받았음을 알림 (200 OK)
        return ApiResponse.of(HttpStatus.OK, null, "입금 및 포인트 충전 완료", null);
    }
}
