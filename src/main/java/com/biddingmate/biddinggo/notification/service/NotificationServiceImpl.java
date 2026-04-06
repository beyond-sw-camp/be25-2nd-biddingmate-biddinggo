package com.biddingmate.biddinggo.notification.service;

import com.biddingmate.biddinggo.common.exception.CustomException;
import com.biddingmate.biddinggo.common.exception.ErrorType;
import com.biddingmate.biddinggo.member.mapper.MemberMapper;
import com.biddingmate.biddinggo.member.service.MemberService;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationRequest;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationResponse;
import com.biddingmate.biddinggo.notification.mapper.NotificationMapper;
import com.biddingmate.biddinggo.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public CreateNotificationResponse createNotification(CreateNotificationRequest request) {

        // 입력값 확인
        if (memberMapper.findById(request.getReceiverId()) == null) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }

        Notification notification = Notification.builder()
                .receiverId(request.getReceiverId())
                .type(request.getType())
                .content(request.getContent())
                .url(request.getUrl())
                .createdAt(LocalDateTime.now())
                .build();

        int result = notificationMapper.insert(notification);

        if(result != 1 || notification.getId() == null){
            throw new CustomException(ErrorType.NOTIFICATOIN_SAVE_FAILED);
        }

        return CreateNotificationResponse.builder()
                .id(notification.getId())
                .build();
    }
}
