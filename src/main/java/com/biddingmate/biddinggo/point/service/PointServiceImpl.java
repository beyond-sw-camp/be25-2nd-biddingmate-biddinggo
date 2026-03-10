package com.biddingmate.biddinggo.point.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.point.mapper.PaymentMybatisMapper;
import com.biddingmate.biddinggo.point.model.PaymentDto;
import com.biddingmate.biddinggo.point.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PaymentMybatisMapper paymentMybatisMapper;
    // private final PointMybatisMapper pointMybatisMapper;

    @Override
    @Transactional
    public CreateVirtualAccountResponse createVirtualAccount(CreateVirtualAccountRequest request) {
        if (paymentMybatisMapper.existsByOrderId(request.getOrderId())) {
            throw new CustomException(ErrorType.VIRTUAL_ACCOUNT_ALREADY_EXISTS);
        }

        PaymentDto payment = PaymentDto.builder()
                .memberId(1L)
                .orderId(request.getOrderId())
                .paymentKey(null)
                .paymentMethod("VIRTUAL_ACCOUNT")
                .amount(request.getAmount())
                .status(PaymentStatus.WAITING_FOR_DEPOSIT) // 여기서 고정
                .approvedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

        int insert = paymentMybatisMapper.insert(payment);

        System.out.println(insert);
        return null;
    }
}
