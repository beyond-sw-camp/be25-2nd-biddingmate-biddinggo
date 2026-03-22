package com.biddingmate.biddinggo.member.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.member.dto.MemberMyResponse;
import com.biddingmate.biddinggo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<MemberMyResponse>> getMyInfo(@RequestParam Long memberId) {
        MemberMyResponse result = memberService.getMyInfo(memberId);
        return ApiResponse.of(HttpStatus.OK, null, "회원 마이페이지 조회 성공", result);
    }
}
