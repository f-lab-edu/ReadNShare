package com.flab.readnshare.domain.notification.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.notification.domain.FCMToken;
import com.flab.readnshare.domain.notification.exception.FCMTokenNotFoundException;
import com.flab.readnshare.domain.notification.repository.FCMTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FCMService {
    private final FCMTokenRepository fcmTokenRepository;

    @Transactional
    public void saveFCMToken(Member member, String token) {
        FCMToken fcmToken = FCMToken.builder()
                .memberId(member.getId())
                .fcmTokenValue(token)
                .build();

        fcmTokenRepository.save(fcmToken);
    }

    public String getFCMToken(Long memberId) {
        return fcmTokenRepository.findById(memberId)
                .orElseThrow(() -> new FCMTokenNotFoundException(memberId))
                .getFcmTokenValue();
    }

    @Transactional
    public void deleteFCMToken(Long memberId) {
        fcmTokenRepository.deleteById(memberId);
    }
}
