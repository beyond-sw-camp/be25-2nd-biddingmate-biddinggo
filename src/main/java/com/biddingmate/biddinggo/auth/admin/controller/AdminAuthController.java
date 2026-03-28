package com.biddingmate.biddinggo.auth.admin.controller;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginRequestDto;
import com.biddingmate.biddinggo.auth.admin.service.AdminAuthService;
import com.biddingmate.biddinggo.common.dto.BaseResponseDto;
import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin-auth", description = "어드민 로그인")
public class AdminAuthController {

    private final AdminAuthService authService;

    @Operation(summary = "로그인", description = "아이디와 패스워드를 JSON 문자열로 받아서 로그인한다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<AdminLoginResponse>> login(
        @RequestBody AdminLoginRequestDto loginRequestDto) {

        AdminLoginResponse loginResponse = authService.login(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()

        );

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, loginResponse));

    }



}
