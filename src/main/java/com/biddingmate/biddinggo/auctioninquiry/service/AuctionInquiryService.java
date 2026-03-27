package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.dto.AuctionInquiryView;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;

public interface AuctionInquiryService {

    CreateAuctionInquiryResponse createInquiry(Long auctionId, Long writerId, CreateAuctionInquiryRequest request);

    AnswerAuctionInquiryResponse registerAnswer(Long inquiryId, Long sellerId, AnswerAuctionInquiryRequest request);

    PageResponse<AuctionInquiryView> getInquiriesByAuctionId(Long auctionId, com.biddingmate.biddinggo.common.request.BasePageRequest request);
}