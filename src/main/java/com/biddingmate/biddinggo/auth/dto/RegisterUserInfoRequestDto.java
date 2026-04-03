package com.biddingmate.biddinggo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserInfoRequestDto {

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입니다.")
    private String nickname;

    @NotBlank(message = "프로필 이미지 URL은 필수입니다.")
    private String imageUrl;

}
