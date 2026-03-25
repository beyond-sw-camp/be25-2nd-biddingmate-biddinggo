package com.biddingmate.biddinggo.auth.jwt;

import com.biddingmate.biddinggo.auth.dto.CustomOAuth2Member;
import com.biddingmate.biddinggo.auth.dto.MemberDto;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }
        }

        if (authorization == null) {

            filterChain.doFilter(request,response);

            // 조건이 해당되면 메소드 종료
            return;
        }

        String token = authorization;

        // 토큰 소멸시간 검증
        if (jwtUtil.isExpired(token)) {

            throw new CustomException(ErrorType.EXPIRED_ACCESS_TOKEN);
        }



        // 토큰에서 username과 role획든
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        MemberDto memberDto = new MemberDto();
        memberDto.setMembername(username);
        memberDto.setRole(role);

        // UserDetails에 회원 정보 객체 담기
        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberDto);

        // 스프링 시큐리티 인증 토큰
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null, customOAuth2Member.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // 토큰 만료 처리
            handlerExceptionResolver.resolveException(request, response, null, new CustomException(ErrorType.EXPIRED_TOKEN));

        } catch (JwtException | IllegalArgumentException e) {
            // 그 외 잘못된 토큰처리
            handlerExceptionResolver.resolveException(request, response, null, new CustomException(ErrorType.INVALID_TOKEN));

        } catch (Exception e) {
            // 그외 예상치 못한 에러처리
            handlerExceptionResolver.resolveException(request, response, null, e);

        }

    }
}
