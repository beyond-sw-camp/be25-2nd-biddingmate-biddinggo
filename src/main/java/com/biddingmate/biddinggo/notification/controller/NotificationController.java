package com.biddingmate.biddinggo.notification.controller;

import com.biddingmate.biddinggo.common.response.ApiResponse;
import com.biddingmate.biddinggo.member.model.Member;
import com.biddingmate.biddinggo.notification.dto.UnreadNotificationResponse;
import com.biddingmate.biddinggo.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name="Notification", description = "알람 api")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "sse 알림 구독")
    public SseEmitter subscribe (@AuthenticationPrincipal Member member) {

        return notificationService.subscribe(member.getId());
    }

    @GetMapping("/unread-count")
    @Operation(summary = "읽지 않은 알림 수 조회")
    public ResponseEntity<ApiResponse<UnreadNotificationResponse>> getUnreadCount (
            @AuthenticationPrincipal Member member) {

        UnreadNotificationResponse result = UnreadNotificationResponse.builder()
                .unreadCount(notificationService.countUnread(member.getId()))
                .build();

        return ApiResponse.of(HttpStatus.OK, null, "읽지 않은 알람 수 조회 성공", result);

    }




}
