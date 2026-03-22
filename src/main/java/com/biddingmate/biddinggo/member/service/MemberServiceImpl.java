package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.dto.MemberMyResponse;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    @Override
    public MemberMyResponse getMyInfo(Long memberId) {

        // 회원 존재 여부 체크
        Member member = memberMapper.findById(memberId);

        // 회원 존재하지 않을 시, 예외처리
        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }

        // 회원 존재 시, 조회
        return memberMapper.selectMyInfo(memberId);
    }
}
