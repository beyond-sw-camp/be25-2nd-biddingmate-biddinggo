package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileResponse {
    private String imageUrl;
    private String name;
    private String nickname;
    private String email;

    private String zipcode;
    private String address;
    private String detailAddress;

    private String bankCode;
    private String bankAccount;

    private LocalDateTime lastChangeNick;
    private Integer remainingDays;
}
