package com.biddingmate.biddinggo.address.controller;

import com.biddingmate.biddinggo.address.dto.CreateAddressRequest;
import com.biddingmate.biddinggo.address.dto.CreateAddressResponse;
import com.biddingmate.biddinggo.address.service.AddressService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateAddressResponse>> createAddress(@RequestBody CreateAddressRequest request,
                                                                            @RequestParam Long memberId) {
        CreateAddressResponse result = addressService.createAddress(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "배송지 등록 성공", result);
    }
}
