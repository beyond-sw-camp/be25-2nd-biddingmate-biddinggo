package com.biddingmate.biddinggo.user.mapper;

import com.biddingmate.biddinggo.user.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    // 이메일로 유저 찾기 (로그인/가입판단용)
    Optional<User> findByEmail(String email);

    // 신규 소셜 로그인 유저 저장
    void save(User user);

}