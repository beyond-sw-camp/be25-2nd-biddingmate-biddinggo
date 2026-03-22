package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.mapper.AuctionInquiryMapper;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
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
        System.out.println("검사 시작 - auctionId: " + auctionId + ", writerId: " + writerId);

        // 1. content 검증
        if (content == null || content.isBlank()) {
            throw new CustomException(ErrorType.AUCTION_INQUIRY_CONTENT_INVALID);
        }

        // 2. 경매 존재 여부 검증
        Auction auction = auctionMapper.findByIdForUpdate(auctionId);
        if (auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        // 본인 경매 문의 제한
        if (auction.getSellerId().equals(writerId)) {
            throw new CustomException(ErrorType.CANNOT_INQUIRE_OWN_AUCTION);
        }

        // 3. 사용자 존재 여부 검증 (Member 구현 시 연결 가능)
        /* [테스트 완료 및 주석 처리]

         */
    /*
    Member member = memberMapper.findById(writerId);
    if (member == null) {
        throw new CustomException(ErrorType.NOT_FOUND);
    }
    */

        Long sellerId = auction.getSellerId();

        AuctionInquiry inquiry = AuctionInquiry.builder()
                .auctionId(auctionId)
                .writerId(writerId)
                .answererId(sellerId)
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