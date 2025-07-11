package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.NotificationTestFixture.TestNotificationContent;
import com.flab.readnshare.domain.notification.exception.InvalidFCMTokenException;
import com.google.api.core.ApiFutures;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.flab.readnshare.NotificationTestFixture.createTestNotificationContent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FCMNotificationSenderTest {

    @Mock
    private FCMService fcmService;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @InjectMocks
    private FCMNotificationSender<TestNotificationContent> fcmNotificationSender;

    @DisplayName("유효한 FCM 토큰이 주어지면 알림을 성공적으로 전송한다.")
    @Test
    void send_notification_success() {
        // given
        Long receiverId = 1L;
        TestNotificationContent content = createTestNotificationContent(receiverId, "테스트 제목", "테스트 내용");
        when(fcmService.getFCMToken(receiverId)).thenReturn("validToken");
        when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn(ApiFutures.immediateFuture("messageId"));

        // when
        fcmNotificationSender.sendNotification(content);

        // then
        verify(firebaseMessaging).sendAsync(any(Message.class));
        verify(fcmService, never()).deleteFCMToken(anyLong());
    }

    @DisplayName("FCM 토큰이 없을 때(null 또는 빈 문자열) 예외가 발생한다.")
    @Test
    void send_notification_with_empty_token_should_throw_exception() {
        // given
        Long receiverId = 1L;
        TestNotificationContent content = createTestNotificationContent(receiverId, "테스트 제목", "테스트 내용");
        when(fcmService.getFCMToken(receiverId)).thenReturn(null);

        // when & then
        assertThrows(InvalidFCMTokenException.class,
                () -> fcmNotificationSender.sendNotification(content));

        verify(firebaseMessaging, never()).sendAsync(any(Message.class));
        verify(fcmService, never()).deleteFCMToken(anyLong());
    }

    @ParameterizedTest
    @MethodSource("messagingErrorCodeProvider")
    @DisplayName("FCM ErrorCode가 INVALID_ARGUMENT 또는 UNREGISTERED일 경우 FCM 토큰을 삭제한다.")
    void send_message_with_invalid_token_should_delete_token(MessagingErrorCode errorCode) {
        // given
        Long receiverId = 1L;
        TestNotificationContent content = createTestNotificationContent(receiverId, "테스트 제목", "테스트 내용");
        when(fcmService.getFCMToken(receiverId)).thenReturn("validToken");

        FirebaseMessagingException fme = mock(FirebaseMessagingException.class);
        when(fme.getMessagingErrorCode()).thenReturn(errorCode);
        when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn(ApiFutures.immediateFailedFuture(fme));

        // when
        fcmNotificationSender.sendNotification(content);

        // then
        verify(fcmService).deleteFCMToken(receiverId);
    }

    private static Stream<MessagingErrorCode> messagingErrorCodeProvider() {
        return Stream.of(MessagingErrorCode.INVALID_ARGUMENT, MessagingErrorCode.UNREGISTERED);
    }
}