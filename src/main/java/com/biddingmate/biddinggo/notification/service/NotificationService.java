package com.biddingmate.biddinggo.notification.service;

import com.biddingmate.biddinggo.notification.dto.CreateNotificationRequest;
import com.biddingmate.biddinggo.notification.dto.CreateNotificationResponse;
import jakarta.validation.Valid;

public interface NotificationService{
    CreateNotificationResponse createNotification(@Valid CreateNotificationRequest request);
}
