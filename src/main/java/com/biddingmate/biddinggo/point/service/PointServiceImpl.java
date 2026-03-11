package com.biddingmate.biddinggo.point.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.util.DateTimeUtils;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.point.dto.TossCreateVirtualAccount;
import com.biddingmate.biddinggo.point.mapper.PaymentMybatisMapper;
import com.biddingmate.biddinggo.point.mapper.VirtualAccountMapper;
import com.biddingmate.biddinggo.point.model.Payment;
import com.biddingmate.biddinggo.point.model.PaymentStatus;
import com.biddingmate.biddinggo.point.model.VirtualAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    @Value("${tosspayments.secret-key}")
    private String secretKey;
    private final WebClient webClient;
    private final PaymentMybatisMapper paymentMybatisMapper;
    private final VirtualAccountMapper virtualAccountMapper;
    // private final PointMybatisMapper pointMybatisMapper;

    @Override
    @Transactional
    public CreateVirtualAccountResponse createVirtualAccount(CreateVirtualAccountRequest request) {
        if (paymentMybatisMapper.existsByOrderId(request.getOrderId())) {
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

        int p_insert = paymentMybatisMapper.insert(payment);
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
}
