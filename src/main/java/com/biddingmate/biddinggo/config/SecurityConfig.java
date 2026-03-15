package com.biddingmate.biddinggo.config;

import com.biddingmate.biddinggo.auth.jwt.JwtAuthenticationFilter;
import com.biddingmate.biddinggo.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. REST API이므로 csrf보안이 필요 없으므로 비활성화 (토큰 기반은 안전)
                .csrf(csrf -> csrf.disable())
                // 2. JWT를 사용하므로 기본 제공하는 폼 로그인과 HTTP basic 인증을 사용하지 않음
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                // 3. 세션 무상태성 (Stateless)설정 : 서버에서 세션을 아예 생성하거나 사용하지 앟음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 4. URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증(로그인) 및 회원가입 관련 API는 누구나 접근 가능
                        .requestMatchers("/api/v1/auth/**", "/api/v1/users/signup").permitAll()
                        // 그외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )

                // 5. JWT 인증 필터 추가 : ID/PW 로그인 필터(UsernamePasswordAuthenticationFilter) 전에
                // 먼저 JWT토큰이 유효한지 검사하는 JwtAuthenticationFilter를 실행하도록 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
