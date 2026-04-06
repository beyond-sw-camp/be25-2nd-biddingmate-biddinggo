package com.biddingmate.biddinggo.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    public SseEmitter subscribe(Long memberId);

    int countUnread(Long memberId);
}
