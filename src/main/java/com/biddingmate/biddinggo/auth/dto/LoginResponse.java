package com.biddingmate.biddinggo.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
@Builder
public class LoginResponse {

    private final String accessToken;
    private final String type;
    private final String username;
    private final List<String> authorities;
    private final long issuedAt;
    private final long expiredAt;

}
