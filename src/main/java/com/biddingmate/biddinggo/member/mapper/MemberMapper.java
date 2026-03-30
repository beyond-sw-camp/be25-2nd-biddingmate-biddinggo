package com.biddingmate.biddinggo.member.mapper;

import com.biddingmate.biddinggo.auth.dto.SocialInfoUpdateDto;
import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.member.dto.MemberBiddingItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileUpdateRequest;
import com.biddingmate.biddinggo.member.dto.MemberWonItemResponse;
import com.biddingmate.biddinggo.member.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper extends IMybatisCRUD<Member> {
    void addPoint(@Param("id") Long id, @Param("amount") Long amount);

    void usePoint(@Param("id") Long id, @Param("amount") Long amount);

    Long getPointById(@Param("id") Long id);

    MemberProfileResponse findProfileById(@Param("memberId") Long memberId);

    // 대시보드
    MemberDashboardResponse findDashboardInfoById(@Param("memberId") Long memberId);

    // 낙찰된 물품 목록
    List<MemberWonItemResponse> findWonItemsById(@Param("memberId") Long memberId);

    // 입찰 중 물품 목록
    List<MemberBiddingItemResponse> findBiddingItemsById(@Param("memberId") Long memberId);

    // 아이디를 통한 사용자 조회(auth)
    Member selectMemberByUsername (@Param("username") String username);

    // 이메일을 통한 사용자 조회(auth)
    Member selectMemberByEmail(@Param("email") String email);


    Member selectMemberByNickname(@Param("nickname") String nickname);

    int saveMember(Member member);

    int updateMember(Member member);

    void updateMemberInfo(SocialInfoUpdateDto updateDto);

    void updateProfile(@Param("memberId") Long memberId, @Param("request") MemberProfileUpdateRequest request);

    // 수정할 닉네임이 사용 중 인지 확인
    int countByNickname(@Param("nickname") String nickname);

    // member status를 DELETED로 변경
    void deleteMember(Long memberId);

}
