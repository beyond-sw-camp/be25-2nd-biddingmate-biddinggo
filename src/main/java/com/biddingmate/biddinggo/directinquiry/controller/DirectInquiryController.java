package com.biddingmate.biddinggo.directinquiry.controller;

import com.biddingmate.biddinggo.directinquiry.dto.AdminInquiryView;
import com.biddingmate.biddinggo.directinquiry.dto.AdminInquiryViewDetail;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerAdminInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.AnswerAdminInquiryResponse;
import com.biddingmate.biddinggo.directinquiry.dto.CreateAdminInquiryRequest;
import com.biddingmate.biddinggo.directinquiry.dto.CreateAdminInquiryResponse;
import com.biddingmate.biddinggo.directinquiry.service.DirectInquiryService;
import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/direct-inquiries")
public class DirectInquiryController {
    private final DirectInquiryService directInquiryService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateAdminInquiryResponse>> createAdminInquiry(
            @RequestBody CreateAdminInquiryRequest request
    ) {
        // 인증 인가 전이므로 writerId를 하드코딩했습니다.
        // 추후에 리펙터링 대상입니다.
        CreateAdminInquiryResponse result = directInquiryService.createAdminInquiry(request);

        return ApiResponse.of(HttpStatus.OK, null, "1대1 문의 성공", result);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<PageResponse<AdminInquiryView>>> findAdminInquiry(BasePageRequest request,
                                                                                        @RequestParam boolean isAdmin,
                                                                                        @RequestParam long memberId) {
        // 인증 인가 완료 전이므로 role 검사 및 memberId 전송
        // 추후에 리펙터링 대상입니다.
        PageResponse<AdminInquiryView> result = directInquiryService.findAdminInquiry(request, isAdmin, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "1대1 문의 목록 조회 성공", result);
    }

    @GetMapping("{inquiryId}")
    public ResponseEntity<ApiResponse<AdminInquiryViewDetail>> findAdminInquiryDetail(@PathVariable Long inquiryId,
                                                                                      @RequestParam boolean isAdmin,
                                                                                      @RequestParam Long memberId) {
        AdminInquiryViewDetail result = directInquiryService.findAdminInquiryDetail(inquiryId, isAdmin, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "1대1 문의 상세 조회 성공", result);
    }

    @PatchMapping("/{inquiryId}")
    // 인증 인가 구현 후 등록 예정
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnswerAdminInquiryResponse>> answerAdminInquiry(@PathVariable Long inquiryId,
                                                                                     @Valid @RequestBody AnswerAdminInquiryRequest request,
                                                                                      @RequestParam Long adminId) {
        AnswerAdminInquiryResponse result = directInquiryService.answerAdminInquiry(inquiryId, request, adminId);

        return ApiResponse.of(HttpStatus.OK, null, "1대1 문의 답변 등록 성공", result);
    }
}
