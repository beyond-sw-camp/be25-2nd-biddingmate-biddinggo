package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.mapper.AuctionInquiryMapper;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionInquiryServiceImpl implements AuctionInquiryService {

    private final AuctionInquiryMapper auctionInquiryMapper;
    private final AuctionMapper auctionMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public CreateAuctionInquiryResponse createInquiry(Long auctionId, Long writerId, String content) {

        // 1. content 검증
        if (content == null || content.isBlank()) {
            throw new CustomException(ErrorType.AUCTION_INQUIRY_CONTENT_INVALID);
        }

        // 2. 경매 존재 여부 검증
        Auction auction = auctionMapper.findById(auctionId);
        if (auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        // 3. 사용자 존재 여부 검증
        Member member = memberMapper.findById(writerId);
        if (member == null) {
            throw new CustomException(ErrorType.NOT_FOUND);
        }

        AuctionInquiry inquiry = AuctionInquiry.builder()
                .auctionId(auctionId)
                .writerId(writerId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        int result = auctionInquiryMapper.insert(inquiry);

        if (result <= 0) {
            throw new CustomException(ErrorType.AUCTION_INQUIRY_CREATE_FAIL);
        }

        return CreateAuctionInquiryResponse.builder()
                .id(inquiry.getId())
                .auctionId(inquiry.getAuctionId())
                .writerId(inquiry.getWriterId())
                .content(inquiry.getContent())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}