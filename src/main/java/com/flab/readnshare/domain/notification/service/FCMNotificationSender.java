package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.notification.domain.NotificationContent;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableRetry
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

        sendMessageWithCallback(message, content);
    }

    @Retryable(
            retryFor = {ServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff (delay = 1000, multiplier = 2)
    )
    private void sendMessageWithCallback(Message message, T content) {
        ApiFuture<String> future = FirebaseMessaging.getInstance().sendAsync(message);

        ApiFutures.addCallback(future, new ApiFutureCallback<String>() {
            @Override
            public void onSuccess(String messageId) {
                log.info("FCM 메시지 전송 성공: messageId={}, receiverId={}",
                        messageId, content.getReceiverId());

            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("FCM 메시지 전송 실패: receiverId={}, 오류={}",
                        content.getReceiverId(), throwable.getMessage());

                handleFailure(content, throwable);
            }

        });
    }

    private void handleFailure(T content, Throwable throwable) {

    }

}
