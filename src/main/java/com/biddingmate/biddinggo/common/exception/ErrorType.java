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
    INVALID_SORT_BY("paging-002", "정렬 기준이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 도메인 별 예시
    // auth
    EXPIRED_ACCESS_TOKEN("auth-001", "만료된 Access Token 입니다.", HttpStatus.CONFLICT),
    INVALID_TOKEN("auth-002", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("auth-003", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("auth-004", "토큰이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),

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
    INVALID_AUCTION_UPDATE_REQUEST("auction-013", "경매 수정 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_AUCTION_CANCEL_REQUEST("auction-014", "경매 취소 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_UPDATE_NOT_ALLOWED("auction-015", "현재 상태에서는 경매를 수정할 수 없습니다.", HttpStatus.CONFLICT),
    AUCTION_CANCEL_NOT_ALLOWED("auction-016", "현재 상태에서는 경매를 취소할 수 없습니다.", HttpStatus.CONFLICT),

    // 검수
    INVALID_INSPECTION_CREATE_REQUEST("inspection-001", "검수 등록 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INSPECTION_SAVE_FAILED("inspection-002", "검수 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INSPECTION_NOT_FOUND("inspection-003", "검수 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_INSPECTION_STATUS("inspection-004", "현재 검수 상태에서는 처리할 수 없습니다.", HttpStatus.CONFLICT),
    INSPECTION_SHIPPING_INFO_ALREADY_EXISTS("inspection-005", "이미 배송 정보가 등록된 검수입니다.", HttpStatus.CONFLICT),
    INSPECTION_SHIPPING_UPDATE_FAILED("inspection-006", "검수 배송 정보 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INSPECTION_LIST_REQUEST("inspection-007", "검수물품 목록 조회 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_INSPECTION_LIST_STATUS("inspection-008", "검수물품 상태값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 파일
    INVALID_FILE_UPLOAD_REQUEST("file-001", "파일 업로드 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    R2_PRESIGNED_URL_GENERATION_FAILED("file-002", "R2 업로드 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("file-003", "파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_LOOKUP_FAILED("file-004", "업로드된 파일 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UPLOADED_FILE_NOT_FOUND("file-005", "업로드된 파일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_UPLOADED_FILE_METADATA("file-006", "업로드된 파일 메타데이터가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 1대1 문의
    ADMIN_INQUIRY_CREATED_FAIL("admin-inquiry-001", "관리자 1대1 문의 생성 실패" , HttpStatus.INTERNAL_SERVER_ERROR),
    ADMIN_INQUIRY_UPDATED_FAIL("admin-inquiry-002", "관리자 1대1 문의 답변 실패" , HttpStatus.INTERNAL_SERVER_ERROR),
    ADMIN_INQUIRY_NOT_FOUND("admin-inquiry-003", "해당 1대1 문의가 존재하질 않습니다.", HttpStatus.NOT_FOUND),
    ADMIN_INQUIRY_ALREADY_ANSWERED("admin-inquiry-004", "해당 1대1 문의는 이미 답변이 완료된 상태입니다.", HttpStatus.CONFLICT),

    // 배송지 관리
    ADDRESS_CREATED_FAIL("address-001", "배송지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    ADDRESS_MAX_COUNT_EXCEEDED("address-002", "배송지는 최대 3개까지 등록 가능합니다.", HttpStatus.CONFLICT),
    ADDRESS_NOT_FOUND("address-003", "해당 배송지는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ADDRESS_UPDATE_DEFAULT_FAIL("address-004", "기본 배송지 변경에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 경매 문의
    AUCTION_INQUIRY_CONTENT_INVALID("auction-inquiry-001", "문의 내용이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_INQUIRY_CREATE_FAIL("auction-inquiry-002", "경매 문의 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_INQUIRE_OWN_AUCTION("auction-inquiry-003", "본인이 등록한 경매에는 문의할 수 없습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_INQUIRY_NOT_FOUND("auction-inquiry-004", "해당 경매 문의가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    AUCTION_INQUIRY_ALREADY_ANSWERED("auction-inquiry-005", "이미 답변이 등록된 문의입니다.", HttpStatus.CONFLICT),
    AUCTION_INQUIRY_UPDATE_FAIL("auction-inquiry-006", "경매 문의 답변 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR),

    // 회원 정보
    MEMBER_NOT_FOUND("member-001", "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    INVALID_NICKNAME_CHANGE_PERIOD("member-002", "닉네임은 30일 이후에 변경할 수 있습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATED_NICKNAME("member-003", "이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST),

    // 입찰
    BID_SAVE_FAILED("bid-001", "입찰 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    BID_AMOUNT_TOO_LOW("bid-002", "입찰 금액이 최소 입찰 가능 금액보다 낮습니다.", HttpStatus.BAD_REQUEST),
    INVALID_BID_UNIT("bid-003", "입찰 단위로만 입찰이 가능합니다.", HttpStatus.BAD_REQUEST),
    BID_AMOUNT_NOT_HIGHER_THAN_PREVIOUS("bid-004", "이전 입찰 기록보다 높게 설정해주세요.", HttpStatus.BAD_REQUEST),
    AUCTION_NOT_BIDDABLE("bid-005", "진행중인 경매에만 입찰을 등록할 수 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_POINT("bid-006", "보유 포인트가 부족합니다.", HttpStatus.BAD_REQUEST),

    // 관심 경매
    WISHLIST_SAVE_FAIL("wishlist-001", "관심 경매 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    WISHLIST_ALREADY_EXISTS("wishlist-002", "해당 관심 경매가 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    WISHLIST_NOT_FOUND("wishlist-003", "해당 관심 경매가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    WISHLIST_DELETE_FAIL("wishlist-004", "관심 경매 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
