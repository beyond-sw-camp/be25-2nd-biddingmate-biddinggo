package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.dto.MemberBiddingItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberWonItemResponse;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    @Override
    public MemberDashboardResponse getMyDashboard(Long memberId) {

        // 회원 존재 여부 체크
        memberExists(memberId);

        // 회원 정보 조회
        MemberDashboardResponse summary = memberMapper.findDashboardInfoById(memberId);

        // 낙찰된 물품 목록 조회
        List<MemberWonItemResponse> wonItems = memberMapper.findWonItemsById(memberId);

        // 입찰 중인 물품 목록 조회
        List<MemberBiddingItemResponse> biddingItems = memberMapper.findBiddingItemsById(memberId);

        // 대시보드 응답 DTO
        return MemberDashboardResponse.builder()
                .nickname(summary.getNickname())
                .grade(summary.getGrade())
                .point(summary.getPoint())
                .wonItems(wonItems)
                .biddingItems(biddingItems)
                .build();
    }

    @Override
    public MemberProfileResponse getMyProfile(Long memberId) {

        // 회원 존재 여부 체크
        memberExists(memberId);

        // 프로필 정보 조회
        return memberMapper.findProfileById(memberId);
    }

    private void memberExists(Long memberId) {
        // 회원 조회
        Member member = memberMapper.findById(memberId);

        // 회원 존재하지 않을 시, 예외처리
        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }
    }
}
