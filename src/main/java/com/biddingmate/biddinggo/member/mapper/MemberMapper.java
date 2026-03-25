package com.biddingmate.biddinggo.member.mapper;

import com.biddingmate.biddinggo.common.inif.IMybatisCRUD;
import com.biddingmate.biddinggo.member.dto.MemberBiddingItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
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
}
