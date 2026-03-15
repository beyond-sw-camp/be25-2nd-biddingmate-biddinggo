package com.biddingmate.biddinggo.auth.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
/** JWT(JSON Web Token) 발급 밒 검증을 담당하는 클래스 (출입증 발급 및 검사소 역할)
 *      - 사용자 로그인 성공 시 Access Token과 Refresh Token을 생성한다.
 *      - 클라이언트의 요청이 들어올 때, 전달받은 토큰이 유효한지 검증하고 정보를 추출한다.
 */
public class JwtProvider {

    private final SecretKey key;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public JwtProvider (
            // @Value 스프리잉 관리하는 설정 파일에 있는 값을 자바 변수로 가져올때 쓰는 어노테이션 (lombok과 다른 햇갈림 주의)
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityTime,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityTime) {

        // yml 파일에 설정된 비밀키를 가져와서 안전한 암호화 키 객체로 만든다.
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityTime = accessTokenValidityTime * 1000L;
        this.refreshTokenValidityTime = refreshTokenValidityTime * 1000L;

    }

    /**
     *
     * @param email 이메일
     * @param role 권한
     *             사용자의 정보(이메일,권한)를 담은 Access Token을 발급
     *             수명이 짧아 탈취되어도 비교적 안정
     */

    public String createAccessToken(String email, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityTime);

        return Jwts.builder()
                .subject(email)                     // 토큰제목
                .claim("role", role)             // 토큰에 담을 추가 정보(권한)
                .issuedAt(now)                      // 발급 시간
                .expiration(validity)               // 만료 시간
                .signWith(key)                      // 서버의 비밀키로 서명(위조 방지)
                .compact();                         // 지금까지의 빌더의 내용을 하나의 긴 jwt문자열로 최종 압축 및 반환
    }

    /**
     * Access Token이 만료되었을 때, 재발급을 받기 위한 Refresh Token을 발급
     * 보안을 위해 사용자 정보(권한 등)는 최소화 하고, 수명이 길다.
     */
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityTime);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }



    /**
     * 유효한 토큰에서 사용자의 이메일(subject)를 꺼내온다
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()                            // 만들어진 토큰을 다시 해석하는 파서
                .verifyWith(key)                        // 파싱할 때 우리가 가진 비밀키로 서명이 일치하는지 검증을 하고 위조되었다면 여기서 예외 발생
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 토큰이 위조되지 않았는지, 만료되지 않앗느닞 유효성을 검사
     * @Return 유효하면 true, 문제(만료, 위조 등)이 있다면 false
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalStateException e) {
            log.error("유효하지 않은 토큰입니다 : {}", e.getMessage());
        }
        return false;
    }



}
