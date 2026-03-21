package com.biddingmate.biddinggo.auctioninquiry.controller;

import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryResponse;
import com.biddingmate.biddinggo.auctioninquiry.service.AuctionInquiryService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction/inquiry")
public class AuctionInquiryController {

    private final AuctionInquiryService auctionInquiryService;

    @PostMapping("/{auctionId}")
    public ResponseEntity<ApiResponse<CreateAuctionInquiryResponse>> createInquiry(
            @PathVariable Long auctionId,
            @Valid @RequestBody CreateAuctionInquiryRequest request
    ) {

        // TODO: 추후 인증/인가 적용 시 로그인한 사용자 ID로 변경 필요
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
}