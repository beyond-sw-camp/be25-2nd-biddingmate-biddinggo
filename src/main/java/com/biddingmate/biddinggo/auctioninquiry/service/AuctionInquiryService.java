package com.biddingmate.biddinggo.auctioninquiry.service;

public interface AuctionInquiryService {

    Long createInquiry(Long auctionId, Long writerId, String content);

}