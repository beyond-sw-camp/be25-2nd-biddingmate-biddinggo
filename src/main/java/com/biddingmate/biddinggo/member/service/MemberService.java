package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileUpdateRequest;

public interface MemberService {
    MemberDashboardResponse getMyDashboard(Long id);

    MemberProfileResponse getMyProfile(Long memberId);

    MemberProfileResponse updateMyProfile(Long memberId, MemberProfileUpdateRequest request);

    void deleteMyAccount(Long memberId);

    long getCurrentPoint(Long memberId);

    void deductPoint(Long memberId, Long amount);
}
