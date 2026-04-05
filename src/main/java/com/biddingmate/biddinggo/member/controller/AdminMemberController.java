package com.biddingmate.biddinggo.member.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.dto.MemberListView;
import com.biddingmate.biddinggo.member.dto.MemberListViewRequest;
import com.biddingmate.biddinggo.member.dto.UpdateMemberStatusRequest;
import com.biddingmate.biddinggo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/members")
public class AdminMemberController {
    private final MemberService memberService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<MemberListView>>> findAllMemberWithFilter(@Valid MemberListViewRequest request) {
        PageResponse<MemberListView> result = memberService.findAllMemberWithFilter(request);

        return ApiResponse.of(HttpStatus.OK, null, "관리자용 모든 사용자의 계정 조회 성공", result);
    }

    @PatchMapping("/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateMemberStatus(@PathVariable Long memberId,
                                                                @RequestBody @Valid UpdateMemberStatusRequest request) {
        memberService.updateMemberStatus(memberId, request);

        return ApiResponse.of(HttpStatus.OK, null, "해당 사용자의 상태 변경에 성공했습니다.", null);
    }
}