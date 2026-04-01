package com.biddingmate.biddinggo.member.dto;

import com.biddingmate.biddinggo.member.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberListView {
    private Long id;
    private String nickname;
    private String email;
    private MemberStatus status;
    private LocalDateTime createdAt;

    // 거래 건수 필드는 추후에 추가 예정
    // private int dealTotalCount;
}
