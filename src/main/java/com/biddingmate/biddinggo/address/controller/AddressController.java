package com.biddingmate.biddinggo.address.controller;

import com.biddingmate.biddinggo.address.dto.AddressListResponse;
import com.biddingmate.biddinggo.address.dto.CreateAddressRequest;
import com.biddingmate.biddinggo.address.dto.CreateAddressResponse;
import com.biddingmate.biddinggo.address.service.AddressService;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<AddressListResponse>>> findAllAddress(@RequestParam Long memberId) {
        List<AddressListResponse> result = addressService.findAllAddress(memberId);

        return ApiResponse.of(HttpStatus.OK, null, "배송지 조회 성공", result);
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> updateDefault(@PathVariable Long addressId,
                                                           @RequestParam Long memberId) {
        addressService.updateDefaultAddress(addressId, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "기본 배송지 변경에 성공했습니다.", null);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long addressId,
                                                           @RequestParam Long memberId) {
        addressService.deleteAddress(addressId, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "배송지 삭제를 성공했습니다.", null);
    }
}
