package com.biddingmate.biddinggo.notification.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.notification.dto.NotificationResponse;
import com.biddingmate.biddinggo.notification.mapper.NotificationMapper;
import com.biddingmate.biddinggo.notification.model.Notification;
import com.biddingmate.biddinggo.notification.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final long SSE_TIMEOUT_MS = 30L * 60L * 1000L;

    private final NotificationMapper notificationMapper;
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(Long memberId) {

        String emitterId = memberId+ "-"+System.currentTimeMillis();
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        emitters.computeIfAbsent(memberId, key -> new ConcurrentHashMap<>())
                .put(emitterId, emitter);

        emitter.onCompletion(() -> removeEmitter(memberId, emitterId));
        emitter.onTimeout(() -> removeEmitter(memberId, emitterId));
        emitter.onError((ex) -> removeEmitter(memberId, emitterId));

        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("connect")
                    .data("connected")
            );
        } catch (IOException e) {
            removeEmitter (memberId, emitterId);
            throw new CustomException(ErrorType.INTERNAL_ERROR);
        }

        return emitter;

    }

    @Override
    public int countUnread(Long memberId) {

        return notificationMapper.countUnreadByReceiverId(memberId);
    }

    @Override
    public void notify(Long receiverId, NotificationType type, String content, String url) {

        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .type(type)
                .content(content)
                .url(url)
                .createdAt(LocalDateTime.now())
                .build();

        int inserted = notificationMapper.insert(notification);

        if (inserted != 1 || notification.getId() == null) {

            throw new CustomException(ErrorType.NOTIFICATION_SAVE_FAILED);

        }

        NotificationResponse payload = NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .content(notification.getContent())
                .url(notification.getUrl())
                .read(false)
                .readAt(null)
                .createdAt(notification.getCreatedAt())
                .build();

        sendToConnectedClients(receiverId, payload);

    }

    private void sendToConnectedClients(Long receiverId, NotificationResponse payload) {

        Map<String, SseEmitter> memberEmitters = emitters.get(receiverId);
        if (memberEmitters == null || memberEmitters.isEmpty()) {
            return;
        }

        for (Map.Entry<String, SseEmitter> entry : memberEmitters.entrySet()) {
            try {
                entry.getValue().send(SseEmitter.event()
                        .id(String.valueOf(payload.getId()))
                        .name("notification")
                        .data(payload)
                );
            } catch (IOException e) {
                removeEmitter(receiverId, entry.getKey());
            }
        }

    }

    private void removeEmitter(Long memberId, String emitterId) {
        Map<String, SseEmitter> memberEmitters = emitters.get(memberId);

        if (emitterId == null) {
            return;
        }

        memberEmitters.remove(emitterId);
        if (memberEmitters.isEmpty()) {
            emitters.remove(memberId);
        }
    }
}
