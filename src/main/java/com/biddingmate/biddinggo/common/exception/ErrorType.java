package com.biddingmate.biddinggo.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    // 공통
    BAD_REQUEST("bad_request", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("unauthorized", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("forbidden", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND("not_found", "리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CONFLICT("conflict", "요청이 현재 상태와 충돌합니다.", HttpStatus.CONFLICT),
    INTERNAL_ERROR("internal_error", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 페이징 처리
    INVALID_SORT_ORDER("paging-001", "정렬 방향이 존재하질 않습니다.", HttpStatus.BAD_REQUEST),

    // 도메인 별 예시
    // auth
    EXPIRED_ACCESS_TOKEN("auth-001", "만료된 Access Token 입니다.", HttpStatus.CONFLICT),

    // 결제
    VIRTUAL_ACCOUNT_ALREADY_EXISTS("payment-001", "가상계좌가 이미 존재합니다.", HttpStatus.CONFLICT),
    TOSS_API_CLIENT_ERROR("payment-002", "토스 API 요청이 잘못되었습니다. 요청 파라미터와 인증 정보를 확인하세요.", HttpStatus.BAD_REQUEST),
    TOSS_API_SERVER_ERROR("payment-003", "토스 서버 내부 오류가 발생했습니다. 잠시 후 다시 시도하세요.", HttpStatus.BAD_GATEWAY),
    PAYMENT_NOT_FOUND("payment-004", "해당 주문의 결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 포인트 히스토리
    POINT_HISTORY_SAVE_FAILED("point-history-001", "포인트 히스토리 저장 실패", HttpStatus.INTERNAL_SERVER_ERROR),

    // 경매
    INVALID_AUCTION_CREATE_REQUEST("auction-001", "경매 등록 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_ITEM_SAVE_FAILED("auction-002", "경매 상품 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    AUCTION_SAVE_FAILED("auction-003", "경매 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ITEM_IMAGE_SAVE_FAILED("auction-004", "상품 이미지 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_ITEM_IMAGE_DISPLAY_ORDER("auction-005", "상품 이미지 노출 순서는 중복될 수 없습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_NOT_FOUND("auction-006", "경매를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("auction-007", "카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_CATEGORY_LEVEL("auction-008", "최하위 카테고리만 선택할 수 있습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_ITEM_NOT_FOUND("auction-009", "경매 등록 대상 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AUCTION_ITEM_SELLER_MISMATCH("auction-010", "상품 판매자와 요청 판매자가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
    INSPECTION_NOT_PASSED("auction-011", "검수 완료된 상품만 경매 등록할 수 있습니다.", HttpStatus.CONFLICT),
    ITEM_NOT_AUCTIONABLE("auction-012", "현재 상태에서는 경매 등록할 수 없습니다.", HttpStatus.CONFLICT),

    // 검수
    INVALID_INSPECTION_CREATE_REQUEST("inspection-001", "검수 등록 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INSPECTION_SAVE_FAILED("inspection-002", "검수 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INSPECTION_NOT_FOUND("inspection-003", "검수 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_INSPECTION_STATUS("inspection-004", "현재 검수 상태에서는 처리할 수 없습니다.", HttpStatus.CONFLICT),
    INSPECTION_SHIPPING_INFO_ALREADY_EXISTS("inspection-005", "이미 배송 정보가 등록된 검수입니다.", HttpStatus.CONFLICT),
    INSPECTION_SHIPPING_UPDATE_FAILED("inspection-006", "검수 배송 정보 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 파일
    INVALID_FILE_UPLOAD_REQUEST("file-001", "파일 업로드 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    R2_PRESIGNED_URL_GENERATION_FAILED("file-002", "R2 업로드 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("file-003", "파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_LOOKUP_FAILED("file-004", "업로드된 파일 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UPLOADED_FILE_NOT_FOUND("file-005", "업로드된 파일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_UPLOADED_FILE_METADATA("file-006", "업로드된 파일 메타데이터가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 1대1 문의
    ADMIN_INQUIRY_CREATED_FAIL("admin-inquiry-001", "관리자 1대1 문의 생성 실패" , HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
