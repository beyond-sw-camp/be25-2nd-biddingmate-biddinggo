package com.biddingmate.biddinggo.auth.admin.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

public interface JWTCookieService {

    ResponseCookie createRefreshTokenCookie(String refreshToken, Duration duration);

    ResponseCookie deleteRefreshTokenCookie();

    HttpHeaders createRefreshTokenCookieHeaders(ResponseCookie cookie);
}
