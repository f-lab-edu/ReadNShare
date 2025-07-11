package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.notification.domain.NotificationContent;
import com.flab.readnshare.domain.notification.exception.InvalidFCMTokenException;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.google.firebase.messaging.MessagingErrorCode.INVALID_ARGUMENT;
import static com.google.firebase.messaging.MessagingErrorCode.UNREGISTERED;

@Component
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationSender<T extends NotificationContent> implements NotificationSender<T> {
    private final FCMService fcmService;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(T content) {
        Long receiverId = content.getReceiverId();
        String token = fcmService.getFCMToken(receiverId);

        if (!(StringUtils.hasText(token))) {
            throw new InvalidFCMTokenException(receiverId);
        }

        Message message = Message
                .builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(content.getTitle())
                                .setBody(content.getBody()).build())
                .setToken(token).build();

        sendMessage(message, receiverId);
    }

    private void sendMessage(Message message, Long receiverId) {
        ApiFuture<String> future = firebaseMessaging.sendAsync(message);

        ApiFutures.addCallback(future, new ApiFutureCallback<String>() {
            @Override
            public void onSuccess(String messageId) {
                log.info("FCM 메시지 전송 성공: messageId={}, receiverId={}", messageId, receiverId);

            }

            @Override
            public void onFailure(Throwable e) {
                log.error("FCM 메시지 전송 실패: receiverId={}, 오류={}", receiverId, e.getMessage());

                handleFailure(receiverId, e);
            }
        }, Runnable::run);
    }

    private void handleFailure(Long receiverId, Throwable e) {
        if (e instanceof FirebaseMessagingException fme) {
            MessagingErrorCode errorCode = fme.getMessagingErrorCode();

            if ((errorCode == INVALID_ARGUMENT) || (errorCode == UNREGISTERED)) {
                log.warn("FCM 토큰 오류 발생: receiverId={}, errorCode={}",
                        receiverId, fme.getMessagingErrorCode());

                fcmService.deleteFCMToken(receiverId);
            }
        } else {
            log.error("FCM 메시지 전송 중 예상치 못한 오류: receiverId={}, error={}"
                    , receiverId, e.getMessage());
        }
    }

}
