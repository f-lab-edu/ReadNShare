package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.notification.domain.NotificationContent;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMNotificationSender<T extends NotificationContent> implements NotificationSender<T> {
    private final FCMService fcmService;

    @Override
    public void sendNotification(T content) {
        String token = fcmService.getFCMToken(content.getReceiverId());

        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(content.getTitle())
                        .setBody(content.getBody())
                        .build())
                .setToken(token)
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }

}
