package com.biddingmate.biddinggo.inspection.service;

import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.inspection.dto.InspectionDetailResponse;
import com.biddingmate.biddinggo.inspection.dto.InspectionListRequest;
import com.biddingmate.biddinggo.inspection.dto.InspectionListResponse;

/**
 * 검수 조회 전용 서비스.
 * 등록/수정 로직과 분리하여 읽기 책임만 담당한다.
 */
public interface InspectionQueryService {
    /**
     * 회원 ID와 선택한 검수 상태를 기준으로 검수물품 목록을 조회한다.
     */
    PageResponse<InspectionListResponse> getInspectionList(InspectionListRequest request, Long memberId);

    /**
     * 검수 ID를 기준으로 상세 정보를 조회한다.
     */
    InspectionDetailResponse getInspectionDetail(Long inspectionId);
}
