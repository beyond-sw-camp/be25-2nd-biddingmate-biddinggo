package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    @Override
    public MemberDashboardResponse getMyDashboard(Long memberId) {

        // 회원 존재 여부 체크
        memberExists(memberId);

        // 대시보드 정보 조회
        return memberMapper.findDashboardById(memberId);
    }

    @Override
    public MemberProfileResponse getMyProfile(Long memberId) {

        // 회원 존재 여부 체크
        memberExists(memberId);

        // 프로필 정보 조회
        return memberMapper.findProfileById(memberId);
    }

    private void memberExists(Long memberId) {
        // 회원 단건 조회
        Member member = memberMapper.findById(memberId);

        // 회원 존재하지 않을 시, 예외처리
        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }
    }
}
