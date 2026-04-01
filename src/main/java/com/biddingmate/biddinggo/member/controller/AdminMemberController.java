package com.biddingmate.biddinggo.member.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.dto.MemberListView;
import com.biddingmate.biddinggo.member.dto.MemberListViewRequest;
import com.biddingmate.biddinggo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/members")
public class AdminMemberController {
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PageResponse<MemberListView>>> findAllMemberWithFilter(@Valid MemberListViewRequest request ) {
        return null;
    }
}
