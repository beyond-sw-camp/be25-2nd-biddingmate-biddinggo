package com.biddingmate.biddinggo.user.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

    private Long id;
    private String email;
    private String nickname;
    private String provider; // 카카오,구글
    private String role; // role_user
}