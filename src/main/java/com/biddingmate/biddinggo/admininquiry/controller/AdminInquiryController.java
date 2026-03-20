package com.biddingmate.biddinggo.admininquiry.controller;

import com.biddingmate.biddinggo.admininquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.admininquiry.dto.CreateAdminInquiryRequest;
import com.biddingmate.biddinggo.admininquiry.dto.CreateAdminInquiryResponse;
import com.biddingmate.biddinggo.admininquiry.model.AdminInquiry;
import com.biddingmate.biddinggo.admininquiry.service.AdminInquiryService;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-inquiries")
public class AdminInquiryController {
    private final AdminInquiryService adminInquiryService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateAdminInquiryResponse>> createAdminInquiry(
            @RequestBody CreateAdminInquiryRequest request
    ) {
        // 인증 인가 전이므로 writerId를 하드코딩했습니다.
        // 추후에 리펙터링 대상입니다.
        CreateAdminInquiryResponse result = adminInquiryService.createAdminInquiry(request);

        return ApiResponse.of(HttpStatus.OK, null, "1대1 문의 성공", result);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<PageResponse<AdminInquiryView>>> findAdminInquiry(BasePageRequest request,
                                                                                        @RequestParam boolean isAdmin,
                                                                                        @RequestParam long memberId) {
        // 인증 인가 완료 전이므로 role 검사 및 memberId 전송
        // 추후에 리펙터링 대상입니다.
        PageResponse<AdminInquiryView> result = adminInquiryService.findAdminInquiry(request, isAdmin, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "1대1 문의 목록 조회 성공", result);
    }
}
