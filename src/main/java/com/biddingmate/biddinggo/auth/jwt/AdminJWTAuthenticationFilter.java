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
public class AdminJWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출
        String token = jwtProvider.resolveToken(request.getHeader("Authorization"));

        System.out.println(token);

        // 토큰 무결성과 유효성 확인 & 블랙리스트 확인 &엑세스 토큰 확인
        if (jwtProvider.isUsableAccessToken(token)) {

            // authentication 객체 생성
            Authentication authentication = jwtProvider.createAuthentication(token);

            // authentication 객체를 SecurityContextHolder 객체에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);


    }
}
