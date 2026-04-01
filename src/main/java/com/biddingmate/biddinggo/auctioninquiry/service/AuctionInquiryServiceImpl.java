package com.biddingmate.biddinggo.auctioninquiry.service;

import com.biddingmate.biddinggo.auction.mapper.AuctionMapper;
import com.biddingmate.biddinggo.auction.model.Auction;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.dto.AuctionInquiryView;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.mapper.AuctionInquiryMapper;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiry;
import com.biddingmate.biddinggo.auctioninquiry.model.AuctionInquiryStatus;
import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.model.MemberRole;
import com.biddingmate.biddinggo.winnerdeal.mapper.WinnerDealMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionInquiryServiceImpl implements AuctionInquiryService {

    private final AuctionInquiryMapper auctionInquiryMapper;
    private final AuctionMapper auctionMapper;
    private final WinnerDealMapper winnerDealMapper;

    @Override
    @Transactional
    public CreateAuctionInquiryResponse createInquiry(Long auctionId, Long writerId, CreateAuctionInquiryRequest request) {

        // 경매 존재 여부 검증
        Auction auction = validateAndGetAuction(auctionId);

        var deal = winnerDealMapper.findByAuctionId(auctionId);
        if (deal == null) {
            // 아직 낙찰되지 않은 경매는 문의 불가
            throw new CustomException(ErrorType.DEAL_NOT_FOUND);
        }
        if (!auction.getWinnerId().equals(writerId)) {
            // 낙찰자가 아니면 문의 불가
            throw new CustomException(ErrorType.INQUIRY_ONLY_FOR_WINNER);
        }

        // 본인 경매 문의 제한
        if (auction.getSellerId().equals(writerId)) {
            throw new CustomException(ErrorType.CANNOT_INQUIRE_OWN_AUCTION);
        }

        AuctionInquiry inquiry = AuctionInquiry.builder()
                .auctionId(auctionId)
                .writerId(writerId)
                .answererId(auction.getSellerId())
                .title(request.getTitle())
                .content(request.getContent())
                .secretYn(request.isSecretYn())
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
                .title(inquiry.getTitle())
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

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuctionInquiryView> getInquiriesByAuctionId(
            Long auctionId,
            BasePageRequest request,
            Long currentUserId,
            MemberRole role
    ) {

        // 경매 존재 여부 확인
        Auction auction = validateAndGetAuction(auctionId);
        boolean isAdmin = role.isAdmin();

        // 페이지 번호 검증
        if (request.getPage() < 1) {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }

        // DB 조회 
        RowBounds rowBounds = new RowBounds(request.getOffset(), request.getSize());
        List<AuctionInquiryView> rawList = auctionInquiryMapper.selectInquiryList(rowBounds, auctionId);
        int totalCount = auctionInquiryMapper.selectInquiryCount(auctionId);

        // 불변 DTO 특성을 살린 권한별 마스킹 처리
        List<AuctionInquiryView> processedList = rawList.stream()
                .map(view -> {
                    // 작성자 본인인가? OR 해당 경매 판매자인가? OR 관리자인가?
                    boolean isWriter = (currentUserId != null) && view.getWriterId().equals(currentUserId);
                    boolean isSeller = (currentUserId != null) && auction.getSellerId().equals(currentUserId);

                    boolean hasFullAccess = isWriter || isSeller || isAdmin;

                    // 권한이 있는 경우: 원본 그대로 반환
                    if (hasFullAccess) {
                        return view;
                    }

                    // 권한이 없는 경우: 닉네임 마스킹 + 비밀글 여부에 따른 내용 마스킹
                    AuctionInquiryView maskedView = view.withMaskedWriterName();
                    if (maskedView.isSecretYn()) {
                        maskedView = maskedView.withSecretMasking();
                    }
                    return maskedView;
                })
                .toList();

        return PageResponse.of(processedList, request.getPage(), request.getSize(), totalCount);
    }

    // 경매 존재 여부 확인 로직 공통 메서드
    private Auction validateAndGetAuction(Long auctionId) {
        Auction auction = auctionMapper.findById(auctionId);
        if (auction == null) {
            throw new CustomException(ErrorType.AUCTION_NOT_FOUND);
        }
        return auction;
    }
}