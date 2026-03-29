package com.biddingmate.biddinggo.point.dto;

import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.point.model.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPointResponse {
    private long currentPoint;
    private PageResponse<PointHistoryDto> histroies;
}
