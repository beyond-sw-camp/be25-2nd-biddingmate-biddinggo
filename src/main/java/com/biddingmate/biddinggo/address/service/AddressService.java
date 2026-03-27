package com.biddingmate.biddinggo.address.service;

import com.biddingmate.biddinggo.address.dto.CreateAddressRequest;
import com.biddingmate.biddinggo.address.dto.CreateAddressResponse;

public interface AddressService {
    CreateAddressResponse createAddress(CreateAddressRequest request, Long memberId);
}
