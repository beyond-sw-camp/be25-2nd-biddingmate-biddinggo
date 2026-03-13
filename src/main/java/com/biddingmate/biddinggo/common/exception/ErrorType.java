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

    // 도메인 별 예시
    // auth
    EXPIRED_ACCESS_TOKEN("auth-001", "만료된 Access Token 입니다.", HttpStatus.CONFLICT),


    // 가상계좌
    VIRTUAL_ACCOUNT_ALREADY_EXISTS("point-001", "가상계좌가 이미 존재합니다.", HttpStatus.CONFLICT),
    TOSS_API_CLIENT_ERROR("point-002", "토스 API 요청이 잘못되었습니다. 요청 파라미터와 인증 정보를 확인하세요.", HttpStatus.BAD_REQUEST),
    TOSS_API_SERVER_ERROR("point-003", "토스 서버 내부 오류가 발생했습니다. 잠시 후 다시 시도하세요.", HttpStatus.BAD_GATEWAY),

    // 경매
    INVALID_AUCTION_CREATE_REQUEST("auction-001", "경매 등록 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    AUCTION_ITEM_SAVE_FAILED("auction-002", "경매 상품 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    AUCTION_SAVE_FAILED("auction-003", "경매 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ITEM_IMAGE_SAVE_FAILED("auction-004", "상품 이미지 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 파일
    INVALID_FILE_UPLOAD_REQUEST("file-001", "파일 업로드 요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    R2_PRESIGNED_URL_GENERATION_FAILED("file-002", "R2 업로드 URL 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED("file-003", "파일 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;
}
