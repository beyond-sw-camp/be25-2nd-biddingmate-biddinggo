package com.biddingmate.biddinggo.notification.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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
