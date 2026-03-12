package com.biddingmate.biddinggo.virtualaccount.service;

import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountResponse;

public interface VirtualAccountService {
    CreateVirtualAccountResponse createVirtualAccount(CreateVirtualAccountRequest request);
}
