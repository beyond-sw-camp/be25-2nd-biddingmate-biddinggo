package com.biddingmate.biddinggo.auth.controller;

import com.biddingmate.biddinggo.auth.dto.CustomOAuth2Member;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthTestController {

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkAuth(
            @AuthenticationPrincipal CustomOAuth2Member oAuth2Member) {

        // 인증되지 않은 경우 (보통 filter에서 걸러지지만 안전장치)
        if (oAuth2Member == null) {
            return ApiResponse.of(HttpStatus.UNAUTHORIZED, "auth-005", "인증 정보가 없습니다.", null);
        }

        // 토큰에서 추출된 유저 정보를 맵에 담아 응답
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", oAuth2Member.getMembername());
        userInfo.put("role", oAuth2Member.getAuthorities());

        return ApiResponse.of(HttpStatus.OK, "success", "인증 서버 검증 완료", userInfo);
    }
}