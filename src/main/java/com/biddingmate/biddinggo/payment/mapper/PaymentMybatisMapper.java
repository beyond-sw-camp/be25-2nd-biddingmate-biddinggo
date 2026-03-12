package com.biddingmate.biddinggo.payment.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.payment.model.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMybatisMapper extends IMybatisCRUD<Payment> {
    boolean existsByOrderId(String orderId);
}
