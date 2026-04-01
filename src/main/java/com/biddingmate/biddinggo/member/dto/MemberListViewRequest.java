package com.biddingmate.biddinggo.member.dto;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberListViewRequest extends BasePageRequest {
    private String keyword;
    private String status;
}
