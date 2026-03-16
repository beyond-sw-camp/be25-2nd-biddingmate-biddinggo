package com.biddingmate.biddinggo.payment.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.util.DateTimeUtils;
import com.biddingmate.biddinggo.payment.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.payment.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.payment.dto.GetVirtualAccountResponse;
import com.biddingmate.biddinggo.payment.dto.TossCreateVirtualAccount;
import com.biddingmate.biddinggo.payment.dto.TossDepositWebhook;
import com.biddingmate.biddinggo.payment.dto.TossPaymentOrderDetails;
import com.biddingmate.biddinggo.payment.mapper.PaymentMapper;
import com.biddingmate.biddinggo.payment.mapper.VirtualAccountMapper;
import com.biddingmate.biddinggo.payment.model.Payment;
import com.biddingmate.biddinggo.payment.model.PaymentStatus;
import com.biddingmate.biddinggo.payment.model.VirtualAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Value("${tosspayments.secret-key}")
    private String secretKey;

    private final WebClient webClient;
    private final PaymentMapper paymentMapper;
    private final VirtualAccountMapper virtualAccountMapper;
    // private final PointMybatisMapper pointMybatisMapper;

    @Override
    @Transactional
    public CreateVirtualAccountResponse createVirtualAccount(CreateVirtualAccountRequest request) {
        if (paymentMapper.existsByOrderId(request.getOrderId())) {
            throw new CustomException(ErrorType.VIRTUAL_ACCOUNT_ALREADY_EXISTS);
        }

        // Authorization 헤더
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        // WebClient POST 호출 (동기 처리)
        TossCreateVirtualAccount responseData = webClient.post()
                .uri("https://api.tosspayments.com/v1/virtual-accounts")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.<Throwable>error(new CustomException(ErrorType.TOSS_API_CLIENT_ERROR)))
                )
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.<Throwable>error(new CustomException(ErrorType.TOSS_API_SERVER_ERROR)))
                )
                .bodyToMono(TossCreateVirtualAccount.class)
                .block(); // 동기 호출

        Payment payment = Payment.builder()
                .memberId(1L)
                .orderId(responseData.getOrderId())
                .paymentKey(responseData.getPaymentKey())
                .paymentMethod(responseData.getMethod())
                .amount(responseData.getTotalAmount())
                .status(PaymentStatus.WAITING_FOR_DEPOSIT)
                .approvedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

        int p_insert = paymentMapper.insert(payment);
        System.out.println("payment 디버깅 = " + p_insert);

        if (responseData.getVirtualAccount() != null) {
            VirtualAccount virtualAccount = VirtualAccount.builder()
                    .paymentId(payment.getId())
                    .bankCode(responseData.getVirtualAccount().getBankCode())
                    .bankAccount(responseData.getVirtualAccount().getAccountNumber())
                    .accountHolderName(responseData.getVirtualAccount().getCustomerName())
                    .dueDate(DateTimeUtils.toLocalDateTime(responseData.getVirtualAccount().getDueDate()))
                    .createdAt(LocalDateTime.now())
                    .build();
            int v_insert = virtualAccountMapper.insert(virtualAccount);
            System.out.println("virtualAccount 디버깅 = " + v_insert);
        }

        // 가상계좌 DTO로 변환
        return CreateVirtualAccountResponse.builder()
                .status(responseData.getStatus())
                .orderId(responseData.getOrderId())
                .amount(responseData.getTotalAmount())
                .bankCode(responseData.getVirtualAccount() != null ? responseData.getVirtualAccount().getBankCode() : null)
                .bankAccount(responseData.getVirtualAccount() != null ? responseData.getVirtualAccount().getAccountNumber() : null)
                .accountHolderName(responseData.getVirtualAccount() != null ? responseData.getVirtualAccount().getCustomerName() : null)
                .dueDate(responseData.getVirtualAccount() != null ? DateTimeUtils.toLocalDateTime(responseData.getVirtualAccount().getDueDate()) : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetVirtualAccountResponse> getVirtualAccount(Long memberId) {
        List<GetVirtualAccountResponse> list =  paymentMapper.findByMemberId(memberId, PaymentStatus.WAITING_FOR_DEPOSIT);

        return list;
    }

    @Override
    public void processDeposit(TossDepositWebhook request) {
        // Authorization 헤더
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        // 승인된 결제를 orderId로 조회
        TossPaymentOrderDetails responseData = webClient.get()
                .uri("https://api.tosspayments.com/v1/payments/orders/" + request.getOrderId())
                .header(HttpHeaders.AUTHORIZATION, authHeader) // 내 시크릿 키 사용
                .retrieve()
                .bodyToMono(TossPaymentOrderDetails.class)
                .block();

        if (responseData == null && "DONE".equals(responseData.getStatus())) {
            System.out.println("여기");
            return;
        }

        LocalDateTime approvedAt = DateTimeUtils.toLocalDateTime(responseData.getApprovedAt());

        // 멱등성 검사
        int updated = paymentRepository.completeIfWaiting(request.getOrderId(), approvedAt);

        if (updated == 0) {
            return; // 이미 처리된 웹훅
        }

    }
}
