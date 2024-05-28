package com.flab.readnshare.domain.notification.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "fcmToken")
@Getter
public class FCMToken {
    @Id
    private Long memberId;
    private String fcmTokenValue;

    @Builder
    public FCMToken(Long memberId, String fcmTokenValue) {
        this.memberId = memberId;
        this.fcmTokenValue = fcmTokenValue;
    }
}
