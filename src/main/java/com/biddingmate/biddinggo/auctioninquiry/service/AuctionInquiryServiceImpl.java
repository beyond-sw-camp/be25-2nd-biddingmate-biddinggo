package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.mapper.AuctionInquiryMapper;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiryStatus;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.model.Member;
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

        // 경매 존재 여부 검증
        Auction auction = auctionMapper.findById(auctionId);
        if (auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }

        // 본인 경매 문의 제한
        if (auction.getSellerId().equals(writerId)) {
            throw new CustomException(ErrorType.CANNOT_INQUIRE_OWN_AUCTION);
        }

        // 사용자 존재 여부 검증

        Member member = memberMapper.findById(writerId);
        if (member == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }

        Long sellerId = auction.getSellerId();

        AuctionInquiry inquiry = AuctionInquiry.builder()
                .auctionId(auctionId)
                .writerId(writerId)
                .answererId(sellerId)
                .content(content)
                .status(AuctionInquiryStatus.PENDING)
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
    @Override
    @Transactional
    public AnswerAuctionInquiryResponse registerAnswer(Long inquiryId, Long sellerId, AnswerAuctionInquiryRequest request) {
        // 문의글 존재 여부 확인
        AuctionInquiry inquiry = auctionInquiryMapper.findInquiryById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorType.AUCTION_INQUIRY_NOT_FOUND));

        // 이미 답변이 달렸는지 확인
        if (inquiry.getStatus() == AuctionInquiryStatus.ANSWERED) {
            throw new CustomException(ErrorType.AUCTION_INQUIRY_ALREADY_ANSWERED);
        }

        // 답변 권한 확인
        if (inquiry.getAnswererId() == null || !inquiry.getAnswererId().equals(sellerId)) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }

        // 답변 데이터 설정
        LocalDateTime now = LocalDateTime.now();
        inquiry.setAnswer(request.getAnswer());
        inquiry.setAnsweredAt(now);
        inquiry.setStatus(AuctionInquiryStatus.ANSWERED);

        int result = auctionInquiryMapper.updateAnswer(inquiry);
        if (result <= 0) {
            throw new CustomException(ErrorType.AUCTION_INQUIRY_UPDATE_FAIL);
        }

        return AnswerAuctionInquiryResponse.builder()
                .id(inquiry.getId())
                .answer(inquiry.getAnswer())
                .answeredAt(now)
                .build();
    }
}