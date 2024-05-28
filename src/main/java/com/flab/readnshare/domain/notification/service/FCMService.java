package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.notification.domain.FCMToken;
import com.flab.readnshare.domain.notification.repository.FCMTokenRepository;
import com.flab.readnshare.global.common.exception.MemberException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final FCMTokenRepository fcmTokenRepository;

    public void saveFCMToken(Member member, String token) {
        FCMToken fcmToken = FCMToken.builder()
                .memberId(member.getId())
                .fcmTokenValue(token)
                .build();

        fcmTokenRepository.save(fcmToken);
    }

    public void sendNotification(Long receiverId, String followerNickName) {
        FCMToken token = fcmTokenRepository.findById(receiverId)
                .orElseThrow(MemberException.MemberNotFoundException::new);

        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("팔로잉")
                        .setBody(followerNickName + "님이 팔로우하기 시작했습니다.")
                        .build())
                .setToken(token.getFcmTokenValue())
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }

}
