package com.biddingmate.biddinggo.auth.mapper;

import com.biddingmate.biddinggo.member.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMemberMapper {

    Member findBySocialInfo(@Param("provider") String provider,
                            @Param("providerId") String providerId);

    int saveMember(Member member);

    int saveSocialAccount(@Param("memberId") Long memberId,
                           @Param("provider") String provider,
                           @Param("providerId") String providerId);

    int updateMember(Member member);

}
