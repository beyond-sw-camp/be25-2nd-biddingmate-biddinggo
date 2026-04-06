package com.biddingmate.biddinggo.notification.service;

import com.biddingmate.biddinggo.notification.model.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long memberId);

    int countUnread(Long memberId);

    void notify(Long receiverId, NotificationType type, String content, String url);
}
