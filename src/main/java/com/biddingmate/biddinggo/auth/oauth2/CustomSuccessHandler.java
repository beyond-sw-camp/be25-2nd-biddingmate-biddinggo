package com.biddingmate.biddinggo.auth.oauth2;

import com.biddingmate.biddinggo.auth.dto.CustomOAuth2Member;
import com.biddingmate.biddinggo.auth.jwt.JwtCookieService;
import com.biddingmate.biddinggo.auth.jwt.JwtProvider;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.member.model.MemberStatus;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationRequest;
import com.biddingmate.biddinggo.notification.model.NotificationType;
import com.biddingmate.biddinggo.notification.service.NotificationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;
    private final MemberMapper memberMapper;
    private final NotificationService notificationService; // for notify (test)

    @Value("${FRONTEND_REDIRECT_URI:http://localhost:5173/oauth/callback}")
    private String frontendRedirectUri;
    @Value("${FRONTEND_REGISTER_REDIRECT_URI:http://localhost:5173/register-info}")
    private String frontendRegisterRedirectUri;

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

        // provider를 통해 토큰 발생

        if (member == null) {
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }

        try {
            notificationService.createNotification(
                    CreateNotificationRequest.builder()
                            .receiverId(member.getId())
                            .type(NotificationType.SOCIAL_LOGIN)
                            .content("소셜 로그인 되었습니다.")
                            .build()
            );

        } catch (Exception e) {
            log.warn("[oauth-login-notification-failed] memberId={}", member.getId(), e);
        }

        String refreshToken = jwtProvider.createRefreshToken(username);
        // JWTCookieService를 사용하여 Refresh 토큰 쿠키 생성 및 응답에 추가
        ResponseCookie cookie = jwtCookieService.createRefreshTokenCookie(refreshToken, Duration.ofDays(1));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String targetUrl = member.getStatus() ==MemberStatus.PENDING
                ? frontendRegisterRedirectUri
                : frontendRedirectUri;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }
}
