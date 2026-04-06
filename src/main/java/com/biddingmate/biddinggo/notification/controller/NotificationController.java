package com.biddingmate.biddinggo.notification.controller;

import com.biddingmate.biddinggo.common.request.BasePageRequest;
import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.common.response.PageResponse;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationRequest;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationResponse;
import com.biddingmate.biddinggo.notification.dto.NotificationResponse;
import com.biddingmate.biddinggo.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    /*
        추후 알림 발생되는 곳에
        모두 알림 등록 프로세스를 구현하면 (notificationService 를 사용하여)
        알림 등록 api 는 삭제 예정
     */
    @PostMapping("/")
    @Operation(summary = "알림 등록", description = "알림을 등록합니다.")
    public ResponseEntity<ApiResponse<CreateNotificationResponse>> createBid(
            @Valid @RequestBody CreateNotificationRequest request
    ) {

        CreateNotificationResponse result = notificationService.createNotification(request);

        return ApiResponse.of(HttpStatus.OK, null, "알림 등록 성공", result);
    }

    @GetMapping("/")
    @Operation(summary = "알림 조회", description = "알림을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotificationsByMemberId(
            BasePageRequest request,
            @AuthenticationPrincipal Member member
    ){

        PageResponse<NotificationResponse> result = notificationService.getNotificationsByMemberId(request, member.getId());

        return ApiResponse.of(HttpStatus.OK, null, "알림 조회 성공", result);
    }

//    @PatchMapping("/read-all")
//    @Operation(summary = "전체 알림 읽음 처리", description = "모든 알림을 읽음 처리합니다.")
//    public ResponseEntity<ApiResponse<NotificationReadResponse>> readAllNotification(
//            @AuthenticationPrincipal Member member
//    ){
//
//        return ApiResponse.of(HttpStatus.OK, null, "전체 알림 읽음 처리", result);
//    }
}
