package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;

public interface MemberService {
    MemberDashboardResponse getMyDashboard(Long id);

    MemberProfileResponse getMyProfile(Long memberId);
}
