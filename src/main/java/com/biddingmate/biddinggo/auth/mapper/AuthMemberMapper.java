package com.biddingmate.biddinggo.auth.mapper;

import com.biddingmate.biddinggo.member.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMemberMapper {

    Member findBySocialInfo(String provider, String providerId);

    void saveMember(Member member);

    void saveSocialAccount(Long id, String provider, String providerId);

    void updateMember(Member member);

}
