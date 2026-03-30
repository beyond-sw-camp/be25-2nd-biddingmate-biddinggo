package com.biddingmate.biddinggo.point.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ExchangePointRequest {
    @NotNull(message = "환전 포인트는 필수입니다.")
    @Min(value = 1, message = "환전 포인트는 1 이상이어야 합니다.")
    private Long amount;
}
