package com.biddingmate.biddinggo.auth.admin.controller;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginRequestDto;
import com.biddingmate.biddinggo.auth.admin.service.AdminAuthService;
import com.biddingmate.biddinggo.common.dto.BaseResponseDto;
import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService authService;

    public ResponseEntity<BaseResponseDto<AdminLoginResponse>> login(
        @RequestBody AdminLoginRequestDto loginRequestDto) {

        AdminLoginResponse loginResponse = authService.login(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()

        );

        return null;





    }


}
