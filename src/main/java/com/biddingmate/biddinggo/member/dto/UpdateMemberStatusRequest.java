package com.biddingmate.biddinggo.member.dto;

import com.biddingmate.biddinggo.member.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberStatusRequest {
    private MemberStatus status;
}
