package com.biddingmate.biddinggo.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int status;         //HTTP 상태 코드(200,400등)
    private String errorCode;   // 에러 발생 시 구분 용 코드
    private String message;     // 사용자에게 보여줄 메세지
    private T result;           // 실제 데이터 (JSON객체)

    // 성공 시 사용하는 매서드
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return of(HttpStatus.OK, null, "요청이 성공적으로 처리되었습니다.", data);
    }

    // 예외를 던지지 않고 직접 에러를 응답할 때 사용
    public static <T> ResponseEntity<ApiResponse<T>> error(String errorCode, String message) {
        return of(HttpStatus.BAD_REQUEST, errorCode, message, null);
    }

    /*
     * 제네릭 정적 팩토리 메서드
     * - 클래스 레벨의 제네릭 <T>는 인스턴스가 생성되는 시점(객체 생성)에 결정되지만,
     *   static 메서드는 객체 생성 없이 호출되므로 클래스의 <T>를 공유할 수 없다.
     * - 따라서, 메서드 반환 타입 앞에 독립적인 제네릭 <T>를 선언하여
     *   메서드 호출 시점에 전달되는 인자(data)의 타입에 따라 유연하게 결정되도록 한다.
     */
    public static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus httpStatus, String errorCode, String message, T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .status(httpStatus.value())
                .errorCode(errorCode)
                .message(message)
                .result(data)
                .build();

        return ResponseEntity.status(httpStatus).body(body);
    }
}
