package com.biddingmate.biddinggo.config;

import com.biddingmate.biddinggo.auth.jwt.JWTFilter;
import com.biddingmate.biddinggo.auth.jwt.JWTUtil;
import com.biddingmate.biddinggo.auth.oauth2.CustomSuccessHandler;
import com.biddingmate.biddinggo.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JWTUtil jwtUtil;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerExceptionResolver handlerExceptionResolver) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((seession) -> seession
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 무한 루프 방지
                .addFilterBefore(new JWTFilter(jwtUtil,handlerExceptionResolver), OAuth2LoginAuthenticationFilter.class)
                // 필터내부 예외 발생시 GlobalExceptionHandler으로 던짐
                .exceptionHandling(exception -> exception
                        // 인증 실패(404)시 GlobalExceptionHandler로 던짐
                        .authenticationEntryPoint((request, response, authException) -> {
                            handlerExceptionResolver.resolveException(request,response,null,authException);
                        })
                        // 인가 거부(403)시 GlobalExceptionHandler로 던짐
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request,response,null, accessDeniedException);
                        })
                )
                // oauth2 로그인 관련
                .oauth2Login((oauth2)-> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customSuccessHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/","/login/**", "/oauth2/**",
                                "/index.html",
                                "/api/v1/payments/**", "/api/v1/files/**",
                                "/api/v1/auctions/**", "/api/v1/inspections/**",
                                "/api/v1/admin-inquiries/**",
                                "/api/v1/bidding/**",
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/api/v1/users/me", "/api/v1/users/me/profile"
                                ).permitAll()
                                .anyRequest().authenticated()
                );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 계속 로그에 에러떠서 추가함
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.ico", "/error");
    }




}
