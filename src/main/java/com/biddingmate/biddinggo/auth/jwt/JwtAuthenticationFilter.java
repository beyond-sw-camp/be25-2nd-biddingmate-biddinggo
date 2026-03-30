package com.biddingmate.biddinggo.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 관리자 경로가 아닐 때는 이 필터가 아무것도 안 하고 넘어가게 함
        // 리펙터링 예정
        if (!request.getRequestURI().startsWith("/api/v1/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String token = jwtProvider.resolveToken(request.getHeader("Authorization"));

        // 토큰 무결성과 유효성 확인 & 블랙리스트 확인 &엑세스 토큰 확인
        if (token != null && jwtProvider.isUsableAccessToken(token)) {

            // authentication 객체 생성
            Authentication authentication = jwtProvider.createAuthentication(token);

            // authentication 객체를 SecurityContextHolder 객체에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }

        filterChain.doFilter(request, response);

    }
}

