package com.biddingmate.biddinggo.virtualaccount.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.virtualaccount.model.VirtualAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VirtualAccountMapper extends IMybatisCRUD<VirtualAccount> {
}
