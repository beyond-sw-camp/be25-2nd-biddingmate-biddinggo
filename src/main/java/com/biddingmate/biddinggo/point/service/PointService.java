package com.biddingmate.biddinggo.point.service;

import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.point.dto.CreateVirtualAccountResponse;

public interface PointService {

    CreateVirtualAccountResponse createVirtualAccount(CreateVirtualAccountRequest request);
}
