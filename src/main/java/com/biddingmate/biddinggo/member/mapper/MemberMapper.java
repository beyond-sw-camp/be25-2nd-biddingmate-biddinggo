package com.biddingmate.biddinggo.member.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends IMybatisCRUD<Member> {
    void addPoint(@Param("id") Long id, @Param("amount") Long amount);

    MemberDashboardResponse findDashboardById(@Param("id") Long id);

    void usePoint(@Param("id") Long id, @Param("amount") Long amount);

    Long getPointById(@Param("id") Long id);

    MemberProfileResponse findProfileById(@Param("id") Long id);

}
