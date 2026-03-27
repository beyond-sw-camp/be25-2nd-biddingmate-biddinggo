package com.biddingmate.biddinggo.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateAddressRequest {
    @NotBlank(message = "우편번호는 필수입니다.")
    private String zipcode;
    @NotBlank(message = "주소는 필수입니다.")
    private String address;
    private String detailAddress;
}
