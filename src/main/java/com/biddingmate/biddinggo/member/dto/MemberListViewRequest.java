package com.biddingmate.biddinggo.member.dto;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.member.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public class MemberListViewRequest extends BasePageRequest {
    private String keyword;
    private MemberStatus status;
}
