package com.biddingmate.biddinggo.auctioninquiry.controller;

import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.dto.AuctionInquiryView;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.service.AuctionInquiryService;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auction Inquiry", description = "경매 문의 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class AuctionInquiryController {

    private final AuctionInquiryService auctionInquiryService;

    @Operation(summary = "경매 문의 등록", description = "특정 경매에 대해 문의를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CreateAuctionInquiryResponse>> createInquiry(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "문의 등록 요청 정보",
                    required = true
            )
            @Valid @RequestBody CreateAuctionInquiryRequest request
    ) {

        Long writerId = 1L;

        CreateAuctionInquiryResponse result =
                auctionInquiryService.createInquiry(
                        request.getAuctionId(),
                        writerId,
                        request
                );

        return ApiResponse.of(
                HttpStatus.OK,
                null,
                "문의 등록 성공",
                result
        );
    }

    @Operation(summary = "경매 문의 답변 등록", description = "판매자가 특정 문의글에 답변을 남깁니다.")
    @PostMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<AnswerAuctionInquiryResponse>> registerAnswer(
            @PathVariable Long inquiryId,
            @Valid @RequestBody AnswerAuctionInquiryRequest request
    ) {

        Long sellerId = 2L;

        AnswerAuctionInquiryResponse result =
                auctionInquiryService.registerAnswer(inquiryId, sellerId, request);

        return ApiResponse.of(
                HttpStatus.OK,
                null,
                "경매 문의 답변 등록 성공",
                result
        );
    }

    @Operation(summary = "경매 문의 목록 조회", description = "해당 경매의 문의글을 페이지 단위로 조회합니다.")
    @GetMapping("/auctions/{auctionId}/inquiries")
    public ResponseEntity<ApiResponse<PageResponse<AuctionInquiryView>>> getInquiryList(
            @PathVariable Long auctionId,
            BasePageRequest request,

            // TODO: 토큰에서 유저 정보 및 권한 추출로 변경 예정
            @RequestParam(required = false, defaultValue = "1") Long currentUserId,
            @RequestParam(required = false, defaultValue = "USER") com.biddingmate.biddinggo.member.model.MemberRole role
    ) {

        PageResponse<AuctionInquiryView> result =
                auctionInquiryService.getInquiriesByAuctionId(auctionId, request,currentUserId, role);

        return ApiResponse.of(
                HttpStatus.OK,
                null,
                "경매 문의 목록 조회 성공",
                result
        );
    }
}