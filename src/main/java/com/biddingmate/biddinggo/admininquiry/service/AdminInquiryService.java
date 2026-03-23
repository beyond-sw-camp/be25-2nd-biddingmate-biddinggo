package com.biddingmate.biddinggo.admininquiry.service;

import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.admininquiry.dto.AnswerAdminInquiryRequest;
import com.biddingmate.biddinggo.admininquiry.dto.AnswerAdminInquiryResponse;
import com.biddingmate.biddinggo.admininquiry.dto.CreateAdminInquiryRequest;
import com.biddingmate.biddinggo.admininquiry.dto.CreateAdminInquiryResponse;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;

public interface AdminInquiryService {
    CreateAdminInquiryResponse createAdminInquiry(CreateAdminInquiryRequest request);
    PageResponse<AdminInquiryView> findAdminInquiry(BasePageRequest request, boolean isAdmin, Long memberId);
    AnswerAdminInquiryResponse answerAdminInquiry(Long inquiryId, AnswerAdminInquiryRequest request, Long adminId);
}
