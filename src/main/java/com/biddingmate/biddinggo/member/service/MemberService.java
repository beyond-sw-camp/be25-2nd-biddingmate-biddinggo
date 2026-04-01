package com.biddingmate.biddinggo.member.service;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.dto.MemberDashboardResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileResponse;
import com.biddingmate.biddinggo.member.dto.MemberProfileUpdateRequest;
import com.biddingmate.biddinggo.member.dto.MemberSalesItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberPurchaseItemResponse;
import com.biddingmate.biddinggo.member.dto.MemberSellingItemResponse;
import jakarta.validation.Valid;

public interface MemberService {
    MemberDashboardResponse getMyDashboard(Long id);

    MemberProfileResponse getMyProfile(Long memberId);

    MemberProfileResponse updateMyProfile(Long memberId, MemberProfileUpdateRequest request);

    void deleteMyAccount(Long memberId);

    PageResponse<MemberSalesItemResponse> getMySales(Long memberId, BasePageRequest pageRequest);
  
    PageResponse<MemberPurchaseItemResponse> getMyPurchases(Long memberId, BasePageRequest pageRequest);

    long getCurrentPoint(Long memberId);

    void deductPoint(Long memberId, Long amount);

    PageResponse<MemberSellingItemResponse> getMySellingItems(Long memberId, String status, BasePageRequest pageRequest);
}
