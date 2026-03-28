package com.biddingmate.biddinggo.point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PointHistoryDto {
    private Long id;
    private String type;
    private long amount;
    private LocalDateTime createdAt;
}