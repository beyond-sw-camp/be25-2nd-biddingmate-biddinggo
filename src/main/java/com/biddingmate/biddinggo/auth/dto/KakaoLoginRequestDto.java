package com.biddingmate.biddinggo.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequestDto {

    // 프론트엔드 -> 백엔드로 카카오 인가코드(?)
    private String code;
}
