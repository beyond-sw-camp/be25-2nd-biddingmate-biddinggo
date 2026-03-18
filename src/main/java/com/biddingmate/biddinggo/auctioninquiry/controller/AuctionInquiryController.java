package com.biddingmate.biddinggo.auctioninquiry.controller;

import com.biddingmate.biddinggo.auctioninquiry.dto.CreateAuctionInquiryRequest;
import com.biddingmate.biddinggo.auctioninquiry.service.AuctionInquiryService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<Long>> createInquiry(
            @PathVariable Long auctionId,
            @RequestBody CreateAuctionInquiryRequest request
    ) {

        Long writerId = 1L;

        Long id = auctionInquiryService.createInquiry(
                auctionId,
                writerId,
                request.getContent()
        );

        return ApiResponse.of(
                HttpStatus.OK,
                null,
                "문의 등록 성공",
                id
        );
    }
}