package com.biddingmate.biddinggo.auth.oauth2.service;

import com.biddingmate.biddinggo.auth.oauth2.dto.CustomOAuth2Member;
import com.biddingmate.biddinggo.auth.oauth2.dto.GoogleResponse;
import com.biddingmate.biddinggo.auth.oauth2.dto.KakaoResponse;
import com.biddingmate.biddinggo.auth.oauth2.dto.MemberDto;
import com.biddingmate.biddinggo.auth.oauth2.dto.OAuth2Response;
import com.biddingmate.biddinggo.auth.oauth2.mapper.AuthMemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthMemberMapper authMemberMapper;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else {

            return null;
        }

        String provider = oAuth2Response.getProvider();
        String providerId = oAuth2Response.getProviderId();
        // JWT에서 식별자로 쓸 이름 -> 우리 db엔 social_account에 각각 들어간다.
        String membername = oAuth2Response.getProvider()+oAuth2Response.getProviderId();

        Member member = authMemberMapper.findBySocialInfo(provider,providerId);

        if (member == null) {

            member = Member.builder()
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .role("USER")
                    .build();

            authMemberMapper.saveMember(member);
            authMemberMapper.saveSocialAccount(member.getId(), provider, providerId);

        } else {

            member.update(oAuth2Response.getName(), oAuth2Response.getEmail());
            authMemberMapper.updateMember(member);

        }

        MemberDto memberDto = new MemberDto();
        memberDto.setMembername(membername);
        memberDto.setRole(member.getRole());
        memberDto.setName(member.getName());

        return new CustomOAuth2Member(memberDto);

    }
}
