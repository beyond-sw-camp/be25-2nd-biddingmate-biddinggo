package com.biddingmate.biddinggo.auth.admin.service;

import com.biddingmate.biddinggo.auth.admin.dto.AdminLoginResponse;

public interface AdminAuthService {

    AdminLoginResponse login(String username, String password);

}
