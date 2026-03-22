package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.member.dto.MemberMyResponse;

public interface MemberService {
    MemberMyResponse getMyInfo(Long memberId);
}
