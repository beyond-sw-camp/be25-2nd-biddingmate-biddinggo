package com.biddingmate.biddinggo.notification.dto;

import com.biddingmate.biddinggo.notification.model.Notification;
import com.biddingmate.biddinggo.notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private NotificationType type;
    private String content;
    private String url;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createAt;
}
