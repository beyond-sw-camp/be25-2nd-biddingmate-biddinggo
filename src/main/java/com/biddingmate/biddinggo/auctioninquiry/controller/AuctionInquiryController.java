package com.biddingmate.biddinggo.auctioninquiry.controller;

import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.AnswerAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.service.AuctionInquiryService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auction Inquiry", description = "경매 문의 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction")
public class AuctionInquiryController {

    private final AuctionInquiryService auctionInquiryService;

    @Operation(summary = "경매 문의 등록", description = "특정 경매에 대해 문의를 등록합니다.")
    @PostMapping("/inquiry/{auctionId}")
    public ResponseEntity<ApiResponse<CreateAuctionInquiryResponse>> createInquiry(

            @Parameter(description = "문의할 경매 ID", example = "1")
            @PathVariable Long auctionId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "문의 등록 요청 정보",
                    required = true
            )
            @Valid @RequestBody CreateAuctionInquiryRequest request
    ) {

        // TODO: 인증/인가 적용 시 로그인 사용자 ID로 변경
        Long writerId = 1L;

        CreateAuctionInquiryResponse result =
                auctionInquiryService.createInquiry(
                        auctionId,
                        writerId,
                        request.getContent()
                );

        return ApiResponse.of(
                HttpStatus.OK,
                null,
                "문의 등록 성공",
                result
        );
    }
    @Operation(summary = "경매 문의 답변 등록", description = "판매자가 특정 문의글에 답변을 남깁니다.")
    @PatchMapping("/answer/{inquiryId}")
    public ResponseEntity<ApiResponse<AnswerAuctionInquiryResponse>> registerAnswer(
            @PathVariable Long inquiryId,
            @Valid @RequestBody AnswerAuctionInquiryRequest request
    ) {
        // TODO: 추후 인증/인가 도입 시 변경 예정

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
}