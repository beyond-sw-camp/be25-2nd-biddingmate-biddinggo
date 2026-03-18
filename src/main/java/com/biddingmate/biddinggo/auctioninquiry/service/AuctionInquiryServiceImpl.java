package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auctioninquiry.mapper.AuctionInquiryMapper;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionInquiryServiceImpl implements AuctionInquiryService {

    private final AuctionInquiryMapper auctionInquiryMapper;

    @Override
    public Long createInquiry(Long auctionId, Long writerId, String content) {

        // 1. content 검증
        if (content == null || content.isBlank()) {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }

        // 2. auctionId 검증
        if (auctionId == null) {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }

        AuctionInquiry inquiry = new AuctionInquiry();

        inquiry.setAuctionId(auctionId);
        inquiry.setWriterId(writerId);
        inquiry.setContent(content);

        auctionInquiryMapper.insertInquiry(inquiry);

        return inquiry.getId();
    }
}