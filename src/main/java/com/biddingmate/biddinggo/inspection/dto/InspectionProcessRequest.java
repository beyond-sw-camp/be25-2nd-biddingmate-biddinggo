package com.biddingmate.biddinggo.inspection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InspectionProcessRequest {
    private boolean approved;
    private String quality;
    private String failureReason;
}
