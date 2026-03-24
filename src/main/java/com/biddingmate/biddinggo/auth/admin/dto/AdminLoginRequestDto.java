package com.biddingmate.biddinggo.auth.admin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class AdminLoginRequestDto {

    private final String username;

    private final String password;
}
