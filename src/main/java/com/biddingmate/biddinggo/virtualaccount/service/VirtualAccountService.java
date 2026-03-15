package com.biddingmate.biddinggo.virtualaccount.service;

import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountRequest;
import com.biddingmate.biddinggo.virtualaccount.dto.CreateVirtualAccountResponse;
import com.biddingmate.biddinggo.virtualaccount.dto.GetVirtualAccountResponse;

import java.util.List;

public interface VirtualAccountService {
    CreateVirtualAccountResponse createVirtualAccount(CreateVirtualAccountRequest request);
    List<GetVirtualAccountResponse> getVirtualAccount(Long id);
}
