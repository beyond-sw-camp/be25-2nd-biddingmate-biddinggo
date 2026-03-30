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

        // 관리자 경로가 아닐 때는 이 필터가 아무것도 안 하고 넘어가게 함
        // 리펙터링 예정
        if (!request.getRequestURI().startsWith("/api/v1/admin")) {
            filterChain.doFilter(request, response);
            return;
        }


        // 토큰 추출
        String token = jwtProvider.resolveToken(request.getHeader("Authorization"));

            // 토큰 무결성과 유효성 확인 & 블랙리스트 확인 &엑세스 토큰 확인
            if (jwtProvider.isUsableAccessToken(token)) {

                // authentication 객체 생성
                Authentication authentication = jwtProvider.createAuthentication(token);

                // authentication 객체를 SecurityContextHolder 객체에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);

    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String requestUri = request.getRequestURI();
//
//        // 관리자 경로가 아닐 때는 이 필터가 아무것도 안 하고 넘어가게 함
//        if (!requestUri.startsWith("/api/v1/admin")) {
//            // 필터 건너뛰는 로그 (필요 시 주석 해제)
//            // System.out.println("[AdminFilter] Skip - Not an Admin Path: " + requestUri);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // [로그] 어드민 필터 진입
//        System.out.println("============== [ADMIN AUTH START] ==============");
//        System.out.println("요청 URI: " + requestUri);
//
//        // 토큰 추출
//        String token = jwtProvider.resolveToken(request.getHeader("Authorization"));
//        System.out.println("헤더 토큰 추출 결과: " + (token != null ? "SUCCESS" : "EMPTY (null)"));
//
//        // 토큰 무결성과 유효성 확인 & 블랙리스트 확인 & 엑세스 토큰 확인
//        if (token != null && jwtProvider.isUsableAccessToken(token)) {
//            try {
//                // authentication 객체 생성
//                Authentication authentication = jwtProvider.createAuthentication(token);
//
//                // authentication 객체를 SecurityContextHolder 객체에 저장
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                // [로그] 인증 성공
//                System.out.println("인증 결과: 성공 (Admin ID: " + authentication.getName() + ")");
//                System.out.println("부여된 권한: " + authentication.getAuthorities());
//            } catch (Exception e) {
//                // [로그] 인증 객체 생성 중 에러 발생 시
//                System.out.println("인증 결과: 에러 발생 (" + e.getMessage() + ")");
//            }
//        } else {
//            // [로그] 토큰이 없거나 유효하지 않은 경우
//            if (token == null) {
//                System.out.println("인증 결과: 실패 (Authorization 헤더가 없거나 토큰이 비어있음)");
//            } else {
//                System.out.println("인증 결과: 실패 (만료, 블랙리스트 등록 또는 잘못된 토큰)");
//            }
//        }
//
//        System.out.println("================================================\n");
//
//        filterChain.doFilter(request, response);
//    }
}

