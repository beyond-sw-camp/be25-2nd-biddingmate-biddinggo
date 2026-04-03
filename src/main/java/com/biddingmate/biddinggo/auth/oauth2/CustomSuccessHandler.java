package com.biddingmate.biddinggo.auth.oauth2;

import com.biddingmate.biddinggo.auth.dto.LoginResponse;
import com.biddingmate.biddinggo.auth.jwt.JwtCookieService;
import com.biddingmate.biddinggo.auth.dto.CustomOAuth2Member;
import com.biddingmate.biddinggo.auth.jwt.JwtProvider;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.member.model.MemberStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;
    private final MemberMapper memberMapper;

    // oauth2 리팩터링
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2Member customUserDetails = (CustomOAuth2Member) authentication.getPrincipal();
        String username = customUserDetails.getMembername();

        log.info("[OAtuh2login] username: {}", username);
        Member member = memberMapper.selectMemberByUsername(username);

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        // provider를 통해 토큰 발생

        if (member == null) {
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }

        Map<String, Object> tokens = jwtProvider.createTotalTokenResponse(username, authorities, member.getStatus().name());
        LoginResponse loginResponse = (LoginResponse) tokens.get("loginResponse");
        String refreshToken = (String) tokens.get("refreshToken");

        String accessToken = loginResponse.getAccessToken();

        // JWTCookieService를 사용하여 Refresh 토큰 쿠키 생성 및 응답에 추가
        ResponseCookie cookie = jwtCookieService.createRefreshTokenCookie(refreshToken, Duration.ofDays(1));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 3. access 토큰은 쿠키가 아닌 리다이렉트 파라미터로 전달해서 프론트엔드가 헤더로 넣을 수있게
        String targetUrl;
        if (member != null && member.getStatus().equals(MemberStatus.PENDING)) {
            targetUrl = "http://localhost:8080/register-info.html";
        } else {
            targetUrl = "http://localhost:8080/success.html";
        }

        // 4. AccessToken을 쿼리 파라미터로 붙여서 리다이렉트
        String finalUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, finalUrl);

    }
}
