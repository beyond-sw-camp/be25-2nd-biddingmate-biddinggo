package com.biddingmate.biddinggo.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final AdminJWTUtil adminJWTUtil;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60L * 24L; // 1일

    public String createAccessToken(String username, List<String> authorities) {


        Map<String, Object> claims =
                Map.of("username", username, "authorities", authorities, "token_type", "access");


        return adminJWTUtil.createJwtToken(claims, ACCESS_TOKEN_EXPIRATION);


    }

    // 클라이언트가 헤더를 통해 서버로 전달한 토큰을 추출
    public String resolveToken(String bearerToken) {

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {

            return bearerToken.substring(7);
        }
        return null;
    }

    // 엑세스 토큰 무결성과 유효성 검증 & 블랙리스트 확인
    public boolean isUsableAccessToken(String accessToken) {

        return accessToken != null
                && adminJWTUtil.validateToken(accessToken)
                && isBlacklisted(accessToken);

    }

    // securityContext 객체에 저장될 Authentication 객체를 생성
    public Authentication createAuthentication(String token) {

        String username = adminJWTUtil.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    public void addBlacklist(String accessToken) {

        String blacklistKey = String.format("blacklist:%S", adminJWTUtil.getJti(accessToken));

        redisTemplate.opsForValue()
                .set(blacklistKey, accessToken, ACCESS_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);


    }

    private boolean isBlacklisted(String accessToken) {

        String blacklistKey = String.format("blacklist:%S", adminJWTUtil.getJti(accessToken));

        return redisTemplate.hasKey(blacklistKey);
    }

    // 리프레시 토큰 제거
    public void deleteRefreshToken(String accessToken) {
        String username = adminJWTUtil.getUsername(accessToken);

        redisTemplate.delete(String.format("refresh:%S", username));

    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String username) {

        Map<String, Object> claims =
                Map.of("username", username, "token_type", "refresh");

        String refreshToken = adminJWTUtil.createJwtToken(claims, REFRESH_TOKEN_EXPIRATION);
        String refreshKey = String.format("refresh:%S", username);

        redisTemplate.opsForValue()
                .set(refreshKey, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

        return refreshToken;

    }
}
