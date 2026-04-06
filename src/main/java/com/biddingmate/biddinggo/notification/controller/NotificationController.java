package com.biddingmate.biddinggo.notification.controller;

import com.biddingmate.biddinggo.bid.dto.CreateBidRequest;
import com.biddingmate.biddinggo.bid.dto.CreateBidResponse;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationRequest;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationResponse;
import com.biddingmate.biddinggo.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    /*
        추후 알림 발생되는 곳에 모두 알림 등록 프로세스를 구현하면 알림 등록 api 는 삭제 예정
     */
    @PostMapping("/")
    @Operation(summary = "알림", description = "알림을 등록합니다.")
    public ResponseEntity<ApiResponse<CreateNotificationResponse>> createBid(
            @Valid @RequestBody CreateNotificationRequest request
    ) {

        CreateNotificationResponse result = notificationService.createNotification(request);

        return ApiResponse.of(HttpStatus.OK, null, "알림 등록 성공", result);
    }
}
