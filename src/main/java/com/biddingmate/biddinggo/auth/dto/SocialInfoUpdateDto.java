package com.biddingmate.biddinggo.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialInfoUpdateDto {

    private String username;
    private String name;
    private String nickname;
    private String status;
}
