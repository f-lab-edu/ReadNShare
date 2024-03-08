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
    private String refreshToken;
    private Long memberId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;

    @Builder
    public RefreshToken(String refreshToken, Long memberId, Long expiration) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.expiration = expiration;
    }
}
