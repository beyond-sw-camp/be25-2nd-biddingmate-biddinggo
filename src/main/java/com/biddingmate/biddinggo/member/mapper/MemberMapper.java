package com.biddingmate.biddinggo.member.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.member.dto.MemberMyResponse;
import com.biddingmate.biddinggo.member.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends IMybatisCRUD<Member> {
    void addPoint(@Param("id") Long id, @Param("amount") Long amount);

    MemberMyResponse selectMyInfo(@Param("memberId") Long memberId);

    void usePoint(@Param("id") Long id, @Param("amount") Long amount);

    Long getPointById(@Param("id") Long id);

    Member findById(Long id);
}
