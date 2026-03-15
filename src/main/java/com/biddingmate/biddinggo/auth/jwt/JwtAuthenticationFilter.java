package com.biddingmate.biddinggo.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
/**
 * 모든 API 요청을 걸러주는 필터
 *  - 사용자가 가져온 JWT를 확인하고, 유효하면 Authentication
 *  - OncePerRequestFilter를 상속받아, 사용자의 요청 한 번당 딱 한 번만 검사를 수행(스프링 시큐리티의 추상 클래스)
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. request에서 토큰 가져옴
        String token = resolveToken(request);

        // 2. token이 있고, 그 출입증이 유효한지 jwtProvider에 문의
        if (token != null && jwtProvider.validateToken(token)) {

            // 3. token이 있다면 이메일 확인
            String email = jwtProvider.getEmailFromToken(token);

            // 4. 스프링 시큐리티 프레임워크가 내부적으로 사용하는 **"이 유저는 검증이 완료된 유저다"**라는 것을 증명하는 전용 객체
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

            // 5. 스프링 시큐리티는 현재 접속 중인 사용자의 인증 상태를 SecurityContext라는 전용 공간(메모리)에 저장합니다. 방금 만든 인증 객체를 이 공간에 저장해 두면,
            // 이후에 실행되는 컨트롤러나 서비스 로직에서 "현재 로그인한 유저 누군지 가져와!" 할 때 바로 꺼내 쓸 수 있게 됩니다.
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authenticationToken);

            log.info("인증 성공! 이메일 : {}", email);
        }

        // 6. 검사 후 다음 필터나 실제 api컨트롤러로 보내기
        filterChain.doFilter(request, response);

    }

    /**
     * Http 해더에서 토큰만 가져오는 매서드
     */
    private String resolveToken(HttpServletRequest request) {
        // Authorization이라는 이름의 값을 가져옴
        String bearerToken = request.getHeader("Authorization");

        // 꺼낸 값이 비어있지 않고, "Bearer"로 시작하는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // "Bearer " 글자(7글자)를 잘라내고 뒤에 있는 실제 토큰 내용만 반환
            return bearerToken.substring(7);

        }
        // 출입증이 없거나 규격에 안 맞으면 null을 반환.
        return null;

    }
}
