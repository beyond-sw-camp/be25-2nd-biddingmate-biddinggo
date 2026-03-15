package com.biddingmate.biddinggo.auth.controller;

import com.biddingmate.biddinggo.auth.dto.AuthResponseDto;
import com.biddingmate.biddinggo.auth.dto.KakaoLoginRequestDto;
import com.biddingmate.biddinggo.auth.service.AuthService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 카카오 로그인 API
     * POST /api/v1/auth/kakao
     */
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<AuthResponseDto>> loginWithKakao(@RequestBody KakaoLoginRequestDto requestDto) {
        log.info("카카오 로그인 요청 발생, 인가 코드 존재 여부 {}", requestDto.getCode() != null);

        // 1. 서비스에서 인가 코드를 전달하며 로그인을 시킨다.
        AuthResponseDto responseDto = authService.loginWithKakao(requestDto.getCode());

        // 2. 서비스가 돌려준 결과를 우리 서버의 공통 응답 규격(ApiResponse)에 담아서 보낸다.
        return ApiResponse.success(responseDto);

    }
}
