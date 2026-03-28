package com.biddingmate.biddinggo.auth.admin.service;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;
import com.biddingmate.biddinggo.auth.jwt.AdminJWTUtil;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminJWTUtil adminJWTUtil;

    @Override
    public AdminLoginResponse login(String username, String password) {

        // username으로 사용자조회
        Member member = memberMapper.selectMemberByUsername(username);

        // db에 있는 비밀 버호 확인
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorType.INVALID_CREDENTIALS);
        }

        return createLoginResponse(member);
    }

    private AdminLoginResponse createLoginResponse(Member member) {

        // 사용자 권한 추출
        List<String> authorities =
                member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        // 엑세스토큰 발급
        String accessToken =
                jwtTokenProvider.createAccessToken(member.getUsername(), authorities);


        return AdminLoginResponse.builder()
                .accessToken(accessToken)
                .type("Bearer")
                .username(member.getUsername())
                .authorities(authorities)
                .issuedAt(jWTUtil.getIssuedAT)
                .build();
    }
}
