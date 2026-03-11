package com.biddingmate.biddinggo.point.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.point.model.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMybatisMapper extends IMybatisCRUD<Payment> {
    boolean existsByOrderId(String orderId);
}
