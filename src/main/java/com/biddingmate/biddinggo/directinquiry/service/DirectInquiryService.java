package com.biddingmate.biddinggo.directinquiry.service;

import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryView;
import com.biddingmate.biddinggo.directinquiry.dto.DirectInquiryViewDetail;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerDirectInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerDirectInquiryResponse;
import com.biddingmate.biddinggo.directinquiry.dto.CreateDirectInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.CreateDirectInquiryResponse;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;

public interface DirectInquiryService {
    CreateDirectInquiryResponse createAdminInquiry(CreateDirectInquiryRequest request);
    PageResponse<DirectInquiryView> findAdminInquiry(BasePageRequest request, boolean isAdmin, Long memberId);
    DirectInquiryViewDetail findAdminInquiryDetail(Long inquiryId, boolean isAdmin, Long memberId);
    AnswerDirectInquiryResponse answerAdminInquiry(Long inquiryId, AnswerDirectInquiryRequest request, Long adminId);
}
