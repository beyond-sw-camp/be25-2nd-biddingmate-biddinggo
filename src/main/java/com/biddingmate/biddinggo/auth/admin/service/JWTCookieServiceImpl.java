package com.biddingmate.biddinggo.auth.admin.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class JWTCookieServiceImpl implements JWTCookieService {
    @Override
    public ResponseCookie createRefreshTokenCookie(String refreshToken, Duration duration) {

        return ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(duration)
                .build();

    }

    @Override
    public HttpHeaders createRefreshTokenCookieHeaders(ResponseCookie cookie) {

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return headers;

    }
}
