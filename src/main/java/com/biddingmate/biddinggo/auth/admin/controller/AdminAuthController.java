package com.biddingmate.biddinggo.auth.admin.controller;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginRequestDto;
import com.biddingmate.biddinggo.auth.admin.dto.AdminSignupRequestDto;
import com.biddingmate.biddinggo.auth.admin.service.AdminAuthService;
import com.biddingmate.biddinggo.auth.admin.service.JWTCookieService;
import com.biddingmate.biddinggo.common.dto.BaseResponseDto;
import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin-auth", description = "어드민 로그인")
public class AdminAuthController {

    private final AdminAuthService authService;
    private final JWTCookieService jwtCookieService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<AdminLoginResponse>> login(
        @Valid @RequestBody AdminLoginRequestDto loginRequestDto) {

        AdminLoginResponse loginResponse = authService.login(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()

        );

        String refreshToken = authService.createRefreshToken(loginResponse.getUsername());
        ResponseCookie cookie =
                jwtCookieService.createRefreshTokenCookie(refreshToken, Duration.ofDays(1));
        HttpHeaders headers = jwtCookieService.createRefreshTokenCookieHeaders(cookie);

        return ResponseEntity.
                ok()
                .headers(headers)
                .body(new BaseResponseDto<>(HttpStatus.OK, loginResponse));

    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponseDto<String>> signup (
            @Valid @RequestBody AdminSignupRequestDto signupRequestDto
            ) {

        authService.signup(signupRequestDto);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.CREATED, "회원가입완료"));


    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(hidden = true) @RequestHeader("Authorization") String bearerToken
    ) {

        authService.logout(bearerToken);

        return ResponseEntity.noContent().build();

    }


}
