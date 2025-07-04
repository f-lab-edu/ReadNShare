package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.notification.domain.NotificationContent;
import com.flab.readnshare.global.common.exception.NotificationException;
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

    @Override
    public void sendNotification(T content) {
        String token = fcmService.getFCMToken(content.getReceiverId());

/*        if (!(StringUtils.hasText(token))) {
            throw new NotificationException.InvalidFCMTokenException(memberId);
        }*/

        Message message = Message
                .builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(content.getTitle())
                                .setBody(content.getBody()).build())
                .setToken(token).build();

        sendMessage(message, content);
    }

    private void sendMessage(Message message, T content) {
        ApiFuture<String> future = FirebaseMessaging.getInstance().sendAsync(message);

        ApiFutures.addCallback(future, new ApiFutureCallback<String>() {
            @Override
            public void onSuccess(String messageId) {
                log.info("FCM 메시지 전송 성공: messageId={}, receiverId={}", messageId, content.getReceiverId());

            }

            @Override
            public void onFailure(Throwable e) {
                log.error("FCM 메시지 전송 실패: receiverId={}, 오류={}", content.getReceiverId(), e.getMessage());

                handleFailure(content, e);
            }
        });
    }

    private void handleFailure(T content, Throwable e) {
        if (e instanceof FirebaseMessagingException fme) {
            MessagingErrorCode errorCode = fme.getMessagingErrorCode();

            if ((errorCode == INVALID_ARGUMENT) || (errorCode == UNREGISTERED)) {
                log.warn("FCM 토큰 오류 발생: receiverId={}, errorCode={}",
                        content.getReceiverId(), fme.getMessagingErrorCode());

                fcmService.deleteFCMToken(content.getReceiverId());
            }
        } else {
            log.error("FCM 메시지 전송 중 예상치 못한 오류: receiverId={}, error={}"
                    , content.getReceiverId(), e.getMessage());
        }
    }

}
