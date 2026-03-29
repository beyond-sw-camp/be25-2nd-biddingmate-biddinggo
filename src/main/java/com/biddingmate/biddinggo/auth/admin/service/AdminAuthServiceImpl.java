package com.biddingmate.biddinggo.auth.admin.service;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;
import com.biddingmate.biddinggo.auth.admin.dto.AdminSignupRequestDto;
import com.biddingmate.biddinggo.auth.dto.SocialInfoUpdateDto;
import com.biddingmate.biddinggo.auth.jwt.JWTProvider;
import com.biddingmate.biddinggo.auth.jwt.AdminJWTUtil;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminJWTUtil adminJWTUtil;
    private final JWTProvider jwtProvider;

    @Override
    public AdminLoginResponse login(String username, String password) {
        // 사용자의 아이디와 비밀번호로 인증 처리를 진행한다.
        // 1. username으로 사용자를 조회
        Member member = memberMapper.selectMemberByUsername(username);

        // 2. PasswordEncoder를 사용해 데이터베이스에 저장된 비밀번호와 입력받은 비밀번호가 일치하는지 확인
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            log.warn("[login-failed] username : {}", username);
            throw new CustomException(ErrorType.INVALID_CREDENTIALS);
        }

        log.info("[Adminlogin] username : {}", username);

        // 3. LoginResponse 객체를 생성해서 반환
        return createLoginResponse(member);
    }

    @Override
    public void signup(AdminSignupRequestDto signupRequestDto) {

        // 아이디 중복 체크
        if (memberMapper.selectMemberByUsername(signupRequestDto.getUsername()) != null) {
            throw new CustomException(ErrorType.DUPLICATE_USERNAME);
        }

        // 이메일 중복 체크
        if (memberMapper.selectMemberByEmail(signupRequestDto.getEmail()) != null) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 체크
        if (memberMapper.selectMemberByNickname(signupRequestDto.getNickname()) != null) {
            throw new CustomException(ErrorType.DUPLICATE_NICKNAME);
        }

        Member member = Member.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .nickname(signupRequestDto.getNickname())
                .role("ADMIN")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        log.info("[signup] username : {}", signupRequestDto.getUsername());

        memberMapper.insert(member);

    }

    @Override
    public void logout(String bearerToken) {
        String accessToken = jwtProvider.resolveToken(bearerToken);

        jwtProvider.addBlacklist(accessToken);
        jwtProvider.deleteRefreshToken(accessToken);

        String username = adminJWTUtil.getUsername(accessToken);
        log.info("[logout] username : {}", username);


    }

    @Override
    public String createRefreshToken(String username) {
        return jwtProvider.createRefreshToken(username);
    }

    @Override
    public AdminLoginResponse refreshAccessToken(String refreshToken) {

        if (refreshToken.isBlank() || !adminJWTUtil.validateToken(refreshToken)) {

            throw new CustomException(ErrorType.REFRESH_TOKEN_INVALID);
        }

        if (!jwtProvider.isValidRefresh(refreshToken)) {

            throw new CustomException(ErrorType.REFRESH_TOKEN_INVALID);

        }

        Member member = memberMapper.selectMemberByUsername(adminJWTUtil.getUsername(refreshToken));

        return createLoginResponse(member);

    }

    @Override
    public void updateInfo(String username, String name, String nickname) {

        Member member = memberMapper.selectMemberByUsername(username);
        if (member == null) {

            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }

        if (memberMapper.selectMemberByNickname(nickname) != null) {

            throw new CustomException(ErrorType.DUPLICATE_NICKNAME);

        }

        SocialInfoUpdateDto updateDto = SocialInfoUpdateDto.builder()
                .username(username)
                .name(name)
                .nickname(nickname)
                .status("ACTIVE")
                .build();

        memberMapper.updateMemberInfo(updateDto);

    }

    private AdminLoginResponse createLoginResponse(Member member) {

        // 사용자 권한 추출
        List<String> authorities =
                member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        // 엑세스토큰 발급
        String accessToken =
                jwtProvider.createAccessToken(member.getUsername(), authorities);


        return AdminLoginResponse.builder()
                .accessToken(accessToken)
                .type("Bearer")
                .username(member.getUsername())
                .authorities(authorities)
                .issuedAt(adminJWTUtil.getIssuedAt(accessToken))
                .expiredAt(adminJWTUtil.getExpiredAt(accessToken))
                .build();
    }



}
