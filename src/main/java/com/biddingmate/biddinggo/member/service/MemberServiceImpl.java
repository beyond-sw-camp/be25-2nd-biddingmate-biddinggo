package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.dto.MemberBiddingItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileUpdateRequest;
import com.biddingmate.biddinggo.member.dto.MemberSalesItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberWonItemResponse;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Override
    public MemberProfileResponse updateMyProfile(Long memberId, MemberProfileUpdateRequest request) {

        // 회원 존재 여부 확인
        Member member = getMember(memberId);

        // 닉네임 변경 요청 시
        if (isNicknameChanged(member, request)) {

            // 마지막 닉네임 변경일 기준 30일 지났는지 체크
            validateNickNameChangePeriod(member);

            // 닉네임 중복 체크
            validateDuplicateNickname(request.getNickname());
        }

        // 프로필 수정
        memberMapper.updateProfile(memberId, request);

        // 수정된 프로필 정보 반환
        return memberMapper.findProfileById(memberId);
    }

    @Override
    public void deleteMyAccount(Long memberId) {

        // 회원 존재 여부 확인
        Member member = getMember(memberId);

        // 이미 탈퇴한 회원이라면 예외 처리
        if ("DELETED".equals(member.getStatus())) {
            throw new CustomException(ErrorType.ALREADY_DELETED_MEMBER);
        }

        // 탈퇴 처리 (soft delete)
        memberMapper.deleteMember(memberId);
    }

    @Override
    public PageResponse<MemberSalesItemResponse> getMySales(Long memberId, BasePageRequest pageRequest) {

        // 회원 존재 여부 확인
        memberExists(memberId);

        // 최신순으로 정렬
        if (pageRequest.getOrder() == null || pageRequest.getOrder().isBlank()) {
            pageRequest.setOrder("DESC");
        }

        // 판매내역 목록 조회
        List<MemberSalesItemResponse> content = memberMapper.findSalesByMember(memberId, pageRequest);

        // 전체 개수 조회
        long totalElements = memberMapper.countSalesByMemberId(memberId);

        return PageResponse.of(
                content,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements
        );
    }

    // 회원 존재 여부 확인
    private void memberExists(Long memberId) {

        Member member = memberMapper.findById(memberId);

        // 회원 존재하지 않을 시, 예외처리
        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }
    }

    // 닉네임 변경 요청 확인 > 요청 닉네임이 존재하고, 공백이 아니고, 기존 닉네임과 다르면 true
    private boolean isNicknameChanged(Member member, MemberProfileUpdateRequest request) {
        return request.getNickname() != null
                && !request.getNickname().isBlank()
                && !Objects.equals(member.getNickname(), request.getNickname());
    }

    // 닉네임 변경 주기 확인 > lastChangeNick이 null이면 최초 변경이므로
    // 마지막 변경일로부터 30일이 지나지 않았으면 예외 발생
    private void validateNickNameChangePeriod(Member member) {
        if (member.getLastChangeNick() == null) {
            return;
        }

        LocalDateTime changeableDate = member.getLastChangeNick().plusDays(30);

        if (LocalDateTime.now().isBefore(changeableDate)) {
            throw new CustomException(ErrorType.INVALID_NICKNAME_CHANGE_PERIOD);
        }
    }

    // 동일한 닉네임이 존재하면 예외 발생
    private void validateDuplicateNickname(String nickname) {
        int count = memberMapper.countByNickname(nickname);

        if (count > 0) {
            throw new CustomException(ErrorType.DUPLICATED_NICKNAME);
        }
    }

    // 회원 조회
    private Member getMember(Long memberId) {
        Member member = memberMapper.findById(memberId);

        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }

        return member;
    }

}
