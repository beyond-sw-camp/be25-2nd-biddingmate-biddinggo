package com.biddingmate.biddinggo.auth.controller;

import com.biddingmate.biddinggo.auth.admin.service.AdminAuthService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminAuthService authService;

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkAuth(Authentication authentication) {

        // 1. 인증 정보가 아예 없는 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.of(HttpStatus.UNAUTHORIZED, "auth-005", "인증 정보가 없습니다.", null);
        }

        // 2. 인증 객체에서 유저 정보 추출
        // authentication.getName()은 우리가 토큰을 만들 때 넣었던 username(google1133...)을 반환합니다.
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("role", authentication.getAuthorities());

        return ApiResponse.of(HttpStatus.OK, "success", "인증 서버 검증 완료", userInfo);
    }

    @PatchMapping("/register")
    @Operation(summary = "필수 정보 입력")
    public ResponseEntity<?> registerUserInfo(
            Authentication authentication,
            @RequestBody Map<String, String> requestData) {

        // 토큰에서 추출된 username (google1133...)
        String username = authentication.getName();

        // 프론트에서 보낸 데이터
        String name = requestData.get("name");
        String nickname = requestData.get("nickname");


        authService.updateInfo(username, name, nickname);

        return ResponseEntity.ok().body(Map.of("message", "success"));

        }


}
