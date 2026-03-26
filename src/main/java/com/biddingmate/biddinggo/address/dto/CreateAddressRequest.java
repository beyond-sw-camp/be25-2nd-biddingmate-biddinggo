package com.biddingmate.biddinggo.address.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateAddressRequest {
    private String zipcode;
    private String address;
    private String detailAddress;
}
