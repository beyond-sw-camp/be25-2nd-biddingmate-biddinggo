package com.biddingmate.biddinggo.auth.service;


import com.biddingmate.biddinggo.auth.dto.AuthResponseDto;
import com.biddingmate.biddinggo.auth.jwt.JwtProvider;
import com.biddingmate.biddinggo.user.mapper.UserMapper;
import com.biddingmate.biddinggo.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${KAKAO_REST_API_KEY}")
    private String clientId;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String clientSecret;

    /**
     * [메인로직] 카카오 로그인 전체 과정
     * @param code 프론트엔드가 카카오에서 받아온 인가코드
     */
    public AuthResponseDto loginWithKakao(String code) {

        // 1. [인가코드]를 카카오 서버에 주고, 그 대가로 [카카오 전용 엑세스 토큰]을 받는다.
        // 이 토큰이 있어야 카카오로부터 유저 정보를 알 수 있음
        String KakaoAccessToken = getKakaoAccessToken(code);

        // 2. 위에서 받은 토큰을 들고 카카오 서버에 다시 가서 유저 정보를 요청
        Map<String, Object> userInfo = getKakaoUserInfo(KakaoAccessToken);

        // 3. 카카오가 보내준 데이터(JSON/Map) 에서 필요한 것만 뽑아낸다
        // 카카오 응답 구조상 kakao_account 안에 profile이 있고, 그 안에 닉네임 등이 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("thumbnail_image_url");

        // 4. DB확인 및 자동 회원가입
        // 일단 이메일로 db 확인
        User user = userMapper.findByEmail(email)
                .orElseGet(() -> {
                    // db에 없으면 회원가입 시작
                    User newUser = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .profileImage(profileImage)
                            .role("ROLE_USER") // 기본 권한은 일반 유저
                            .build();
                    userMapper.save(newUser); // db에 저장
                    return newUser;
                });

        // 5. 우리서버 전용 JWT 토큰 발급
        // 이제 신분 확인이 끝났으니, 우리 서버에서 앞으로 사용할 JWT를 발급
        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        // 6. 프론트엔드에게 최종결과(토큰 + 유저정보)를 전달
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();

    }

    /**
     * [카카오 통신 로직 1] 인가 코드를 토큰으로 교환
     */
    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // 헤더 설정 : 데이터의 폼 형식(URL Encoded)라고 알림
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 바디 설정 : 카카오가 요구하는 필수 데이터를 채운다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);      // 내 REST API키
        params.add("client_secret", clientSecret);      // 내 보안키
        params.add("redirect_uri", "http://localhost:8080/api/v1/auth/kakao/callback"); // 카카오 설정과 일치해야함
        params.add("code", code);       // 프론트가 준 코드

        // 요청 포장(해더 + 바디)
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 카카오 서버에 POST방식으로 요청을 보내고 응답을 Map으로 받는다.
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        // 응답 받은 뭉치에서 "access_token" 글자만 꺼내서 반환
        return (String) response.getBody().get("access_token");
    }

    /**
     * [카카오 통신 로직 2] 카카오 토큰으로 실제 유저 개ㅔ인정보를 가져오는 함수
     */
    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // 헤더 설정: "나 카카오 토큰(Bearer) 가지고 있어!"라고 인증 정보를 실어 보냅니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // "Authorization: Bearer [토큰]" 형식으로 자동 생성
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 바디 없이 헤더만 담아서 요청 준비
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // 카카오 서버에 GET 방식으로 요청을 보내서 유저 정보를 Map으로 통째로 받습니다.
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

        return response.getBody();
    }
}
