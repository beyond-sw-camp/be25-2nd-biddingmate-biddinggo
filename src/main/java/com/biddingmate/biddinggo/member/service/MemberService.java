package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.member.dto.MemberMyResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;

public interface MemberService {
    MemberMyResponse getMyInfo(Long memberId);

    MemberProfileResponse getMyProfile(Long memberId);
}
