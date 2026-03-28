package com.biddingmate.biddinggo.point.service;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.point.dto.MyPointResponse;

public interface PointService {
    MyPointResponse findMyPointList(BasePageRequest request, Long memberId);
}
