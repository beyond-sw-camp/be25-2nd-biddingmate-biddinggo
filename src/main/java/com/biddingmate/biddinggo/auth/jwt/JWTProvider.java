package com.biddingmate.biddinggo.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final AdminJWTUtil adminJWTUtil;
//    private final UserDetailsService userDetailsService;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 30; // 30분

    public String createAccessToken(String username, List<String> authorities) {


        Map<String, Object> claims =
                Map.of("username", username, "authorities", authorities, "token_type", "access");


        return adminJWTUtil.createJwtToken(claims, ACCESS_TOKEN_EXPIRATION);


    }


}
