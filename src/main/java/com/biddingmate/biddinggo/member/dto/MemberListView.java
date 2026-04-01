package com.biddingmate.biddinggo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberListView {
    private Long memberId;
    private String nickname;
    private String email;
    private String status;
    private LocalDateTime createdAt;
    private int totalCount;
}
