package com.biddingmate.biddinggo.point.controller;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.point.dto.ExchangePointRequest;
import com.biddingmate.biddinggo.point.dto.MyPointResponse;
import com.biddingmate.biddinggo.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyPointResponse>> findMyPointList(BasePageRequest request,
                                                                        @RequestParam Long memberId) {
        MyPointResponse result = pointService.findMyPointList(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "마이페이지 포인트 내역 조회에 성공했습니다.", result);
    }

    @PostMapping("/exchanges")
    public ResponseEntity<ApiResponse<Void>> exchangePoint(@Valid @RequestBody ExchangePointRequest request, @RequestParam Long memberId) {
        pointService.exchangePoint(request, memberId);

        return ApiResponse.of(HttpStatus.OK, null, "포인트 환전에 성공했습니다.", null);
    }
}
