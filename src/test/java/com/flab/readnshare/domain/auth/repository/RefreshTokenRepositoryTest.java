package com.flab.readnshare.domain.auth.repository;

import com.flab.readnshare.domain.auth.domain.RefreshToken;
import com.flab.readnshare.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @DisplayName("refreshToken을 저장하고 Id로 조회한다.")
    @Test
    void save_and_find_by_id_success() {
        // given
        String token = "testToken";
        Long memberId = 1L;
        Long expiration = 60000L;

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenValue(token)
                .memberId(memberId)
                .expiration(expiration)
                .build();

        // when
        refreshTokenRepository.save(refreshToken);

        // then
        Optional<RefreshToken> foundToken = refreshTokenRepository.findById(token);
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getMemberId()).isEqualTo(memberId);
        assertThat(foundToken.get().getRefreshTokenValue()).isEqualTo("testToken");
    }

    @DisplayName("만료 시간이 지나면 refreshToken이 자동으로 삭제된다.")
    @Test
    void token_expires_after_ttl() throws InterruptedException {
        // given
        String token = "expireTestToken";
        Long memberId = 2L;
        Long expiration = 2000L; // 2초

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenValue(token)
                .memberId(memberId)
                .expiration(expiration)
                .build();

        refreshTokenRepository.save(refreshToken);

        // when
        Thread.sleep(2100);

        // then
        Optional<RefreshToken> foundToken = refreshTokenRepository.findById(token);
        assertThat(foundToken).isNotPresent();
    }

}