package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.notification.domain.FCMToken;
import com.flab.readnshare.domain.notification.repository.FCMTokenRepository;
import com.flab.readnshare.global.common.exception.NotificationException;
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

    public String getFCMToken(Long memberId) {
        return fcmTokenRepository.findById(memberId)
                .orElseThrow(() -> new NotificationException.FCMTokenNotFoundException(memberId))
                .getFcmTokenValue();
    }
}
