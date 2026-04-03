package com.biddingmate.biddinggo.point.service;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.point.dto.ExchangePointRequest;
import com.biddingmate.biddinggo.point.dto.MyPointResponse;
import com.biddingmate.biddinggo.point.model.PointHistory;

public interface PointService {
    int addPointHistory(PointHistory pointHistory);

    MyPointResponse findMyPointList(BasePageRequest request, Long memberId);
    void exchangePoint(ExchangePointRequest request, Long memberId);
    void refundBid(Long bidderId, Long amount);
}
