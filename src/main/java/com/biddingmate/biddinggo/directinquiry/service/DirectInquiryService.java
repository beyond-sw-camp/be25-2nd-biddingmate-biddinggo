package com.biddingmate.biddinggo.directinquiry.service;

import com.biddingmate.biddinggo.directinquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.directinquiry.dto.AdminInquiryViewDetail;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerAdminInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerAdminInquiryResponse;
import com.biddingmate.biddinggo.directinquiry.dto.CreateAdminInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.CreateAdminInquiryResponse;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;

public interface DirectInquiryService {
    CreateAdminInquiryResponse createAdminInquiry(CreateAdminInquiryRequest request);
    PageResponse<AdminInquiryView> findAdminInquiry(BasePageRequest request, boolean isAdmin, Long memberId);
    AdminInquiryViewDetail findAdminInquiryDetail(Long inquiryId, boolean isAdmin, Long memberId);
    AnswerAdminInquiryResponse answerAdminInquiry(Long inquiryId, AnswerAdminInquiryRequest request, Long adminId);
}
