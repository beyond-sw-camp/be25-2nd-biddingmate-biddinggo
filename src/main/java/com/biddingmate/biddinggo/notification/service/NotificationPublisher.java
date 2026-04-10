package com.biddingmate.biddinggo.notification.service;

import com.biddingmate.biddinggo.notification.dto.CreateNotificationRequest;
import com.biddingmate.biddinggo.notification.model.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

    private final NotificationService notificationService;

    // 알람 발신을 위한 매서드
    public void publishNotification(Long receiverId, NotificationType type, String content, String url) {

        try {
            notificationService.createNotification(
                    CreateNotificationRequest.builder()
                            .receiverId(receiverId)
                            .type(type)
                            .content(content)
                            .url(url)
                            .build()
            );
        } catch (Exception e) {

            log.warn("[notification-failed] receiverId={}, type={}", receiverId, type, e);
        }
    }

}
