package com.biddingmate.biddinggo.inspection.service;

import com.biddingmate.biddinggo.inspection.dto.CreateInspectionRequest;

/**
 * inspection 테이블 저장 책임을 담당하는 서비스.
 */
public interface InspectionService {
    /**
     * 이미 생성된 itemId를 기준으로 inspection 데이터를 저장한다.
     */
    Long createInspection(CreateInspectionRequest request, Long itemId);
}
