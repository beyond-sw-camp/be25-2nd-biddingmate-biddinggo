package com.biddingmate.biddinggo.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String imageUrl;
    private Long point;
    private String bankCode;
    private String bankAccount;
    private String grade;
    private String role;
    private String status;
    private LocalDateTime lastChangeNick;
    private LocalDateTime createdAt;

    public void update(String name, String email) {

        this.name = name;
        this.email = email;

    }
}
