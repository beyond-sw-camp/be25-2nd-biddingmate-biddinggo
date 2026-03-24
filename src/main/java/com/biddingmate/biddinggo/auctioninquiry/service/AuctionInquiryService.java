package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;

public interface AuctionInquiryService {

    CreateAuctionInquiryResponse createInquiry(Long auctionId, Long writerId, String content);

}