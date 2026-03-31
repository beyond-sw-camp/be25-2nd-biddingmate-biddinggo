package com.biddingmate.biddinggo.inspection.dto;

import com.biddingmate.biddinggo.item.model.ItemInspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AdminInspectionListResponse {
    private Long inspectionId;
    private Long itemId;
    private String name;
    private ItemInspectionStatus status;
    private String quality;
    private LocalDateTime createdAt;
}
