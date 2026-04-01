package com.biddingmate.biddinggo.inspection.controller;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.inspection.dto.AdminInspectionListRequest;
import com.biddingmate.biddinggo.inspection.dto.AdminInspectionListResponse;
import com.biddingmate.biddinggo.inspection.dto.InspectionListRequest;
import com.biddingmate.biddinggo.inspection.service.InspectionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/inspections")
public class AdminInspectionController {
    private final InspectionQueryService inspectionQueryService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<AdminInspectionListResponse>>> findAllDirectInquiry(
            AdminInspectionListRequest request) {
        PageResponse<AdminInspectionListResponse> result = inspectionQueryService.findAllWithFilter(request);

        return ApiResponse.of(HttpStatus.OK, null, "관리자 검수 요청 물품 조회 성공", result);
    }
}
