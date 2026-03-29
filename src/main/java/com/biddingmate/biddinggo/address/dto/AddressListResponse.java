package com.biddingmate.biddinggo.address.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AddressListResponse {
    private Long id;
    private String zipcode;
    private String address;
    private String detailAddress;
    private boolean defaultYn;
    private LocalDateTime createdAt;
}
