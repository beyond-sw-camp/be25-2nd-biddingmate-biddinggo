package com.biddingmate.biddinggo.member.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.member.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends IMybatisCRUD<Member> {
    void addPoint(@Param("id") Long id, @Param("amount") Long amount);

}
