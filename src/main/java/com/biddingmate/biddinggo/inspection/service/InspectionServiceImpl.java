package com.biddingmate.biddinggo.inspection.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.inspection.dto.CreateInspectionRequest;
import com.biddingmate.biddinggo.inspection.mapper.InspectionMapper;
import com.biddingmate.biddinggo.inspection.model.Inspection;
import com.biddingmate.biddinggo.inspection.model.InspectionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * inspection 엔티티 생성만 담당하는 서비스 구현체.
 * 트랜잭션은 상위 애플리케이션 서비스에서 시작된 흐름에 참여한다.
 */
@Service
@RequiredArgsConstructor
public class InspectionServiceImpl implements InspectionService {
    private final InspectionMapper inspectionMapper;

    @Override
    /**
     * 검수 대상 itemId를 기준으로 inspection 레코드를 생성한다.
     * 최초 등록 상태는 항상 PENDING으로 시작한다.
     */
    public Long createInspection(CreateInspectionRequest request, Long itemId) {
        if (itemId == null) {
            throw new CustomException(ErrorType.INVALID_INSPECTION_CREATE_REQUEST);
        }

        // 검수 발송 정보는 선택 입력이므로 null 여부를 분기해서 사용한다.
        CreateInspectionRequest.Inspection inspectionRequest = request.getInspection();

        // inspection 테이블 저장용 모델로 변환한다.
        Inspection inspection = Inspection.builder()
                .itemId(itemId)
                .status(InspectionStatus.PENDING)
                .carrier(inspectionRequest != null ? inspectionRequest.getCarrier() : null)
                .trackingNumber(inspectionRequest != null ? inspectionRequest.getTrackingNumber() : null)
                .createdAt(LocalDateTime.now())
                .build();

        // inspection 저장 후 생성된 PK를 모델에 주입받는다.
        int inspectionInsertCount = inspectionMapper.insert(inspection);

        if (inspectionInsertCount != 1 || inspection.getId() == null) {
            throw new CustomException(ErrorType.INSPECTION_SAVE_FAILED);
        }

        return inspection.getId();
    }
}
