package com.biddingmate.biddinggo.auth.admin.service;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;
import com.biddingmate.biddinggo.auth.admin.dto.AdminSignupRequestDto;

public interface AdminAuthService {

    AdminLoginResponse login(String username, String password);

    void signup(AdminSignupRequestDto signupRequestDto);

    void logout(String bearerToken);
}
