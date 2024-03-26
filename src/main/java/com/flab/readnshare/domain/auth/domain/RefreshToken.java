package com.flab.readnshare.domain.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "refreshToken")
@Getter
public class RefreshToken {
    @Id
    private String refreshTokenValue;
    private Long memberId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;

    @Builder
    public RefreshToken(String refreshTokenValue, Long memberId, Long expiration) {
        this.refreshTokenValue = refreshTokenValue;
        this.memberId = memberId;
        this.expiration = expiration;
    }
}
