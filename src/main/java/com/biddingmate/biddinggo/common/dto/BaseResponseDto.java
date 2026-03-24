package com.biddingmate.biddinggo.common.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@ToString
@Getter
public class BaseResponseDto<T> {

    protected final int code;

    private final String message;

    private final List<T> item;

    public BaseResponseDto(HttpStatus httpStatus, T item) {

        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.item = Collections.singletonList(item);

    }

    protected BaseResponseDto(HttpStatus httpStatus, List<T> items) {

        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.item = items;
    }


}
