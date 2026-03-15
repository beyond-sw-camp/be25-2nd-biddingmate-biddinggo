package com.biddingmate.biddinggo.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    // 로그인 성공 후 백엔드->프론트엔드
    private String accessToken;
    private String refreshToken;
    private String email;
    private String role;

}
