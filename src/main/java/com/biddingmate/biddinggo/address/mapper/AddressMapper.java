package com.biddingmate.biddinggo.address.mapper;

import com.biddingmate.biddinggo.address.model.Address;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AddressMapper extends IMybatisCRUD<Address> {
    int countByMemberId(@Param("memberId") Long memberId);
}
