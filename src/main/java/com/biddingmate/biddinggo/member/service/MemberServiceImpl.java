package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.dto.MemberMyResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileQueryResponse;
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

    @Override
    public MemberProfileResponse getMyProfile(Long memberId) {

        // 회원 존재 여부 체크
        memberExists(memberId);

        MemberProfileQueryResponse queryResult = memberMapper.selectMyProfile(memberId);

        boolean canChangeNickname = false;
        int remainingDays = 0;

        // 닉네임을 한번도 변경하지 않았을 경우
        if (queryResult.getLastChangeNick() == null) {
            canChangeNickname = true;

            // 닉네임을 변경한 적이 있을 경우
        } else {
            LocalDate today = LocalDate.now();
            LocalDate changeableDate = queryResult.getLastChangeNick().toLocalDate().plusDays(30);

            // 30일이 지나야 닉네임 변경가능, 30일 되는 날은 불가능
            if (today.isAfter(changeableDate)) {
                canChangeNickname = true;
            } else {
                remainingDays = (int) ChronoUnit.DAYS.between(today, changeableDate);
            }
        }

        return MemberProfileResponse.builder()
                .imageUrl(queryResult.getImageUrl())
                .name(queryResult.getName())
                .nickname(queryResult.getNickname())
                .email(queryResult.getEmail())
                .zipcode(queryResult.getZipcode())
                .address(queryResult.getAddress())
                .detailAddress(queryResult.getDetailAddress())
                .bankCode(queryResult.getBankCode())
                .bankAccount(queryResult.getBankAccount())
                .canChangeNickname(canChangeNickname)
                .remainingDays(remainingDays)
                .build();
    }

    private void memberExists(Long memberId) {
        Member member = memberMapper.findById(memberId);

        // 회원 존재하지 않을 시, 예외처리
        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }
    }
}
