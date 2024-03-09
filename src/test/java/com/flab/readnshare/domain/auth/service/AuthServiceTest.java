package com.flab.readnshare.domain.auth.service;

import com.flab.readnshare.domain.auth.dto.SignInRequestDto;
import com.flab.readnshare.domain.auth.repository.RefreshTokenRepository;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.global.common.exception.AuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("로그인을 하면 member를 리턴한다")
    void signIn_success_return_member() {
        // given
        SignInRequestDto request = SignInRequestDto.builder()
                .email("test@naver.com")
                .password("test24680!")
                .build();

        Member expectedMember = Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(expectedMember));

        // when
        Member foundMember = authService.signIn(request);

        // then
        assertNotNull(foundMember);
        assertEquals(request.getEmail(), foundMember.getEmail());
    }

    @Test
    @DisplayName("비밀번호 불일치 시 InvalidPasswordException이 발생한다")
    void signIn_fail_password() {
        // given
        SignInRequestDto request = SignInRequestDto.builder()
                .email("test@naver.com")
                .password("test24680!")
                .build();

        Member expectedMember = Member.builder()
                .email(request.getEmail())
                .password("test12345!")
                .build();

        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(expectedMember));

        // when & then
        assertThrows(AuthException.InvalidPasswordException.class, () -> authService.signIn(request));
    }

    @Test
    @DisplayName("Refresh token이 Redis에 없는 경우 ExpiredTokenException이 발생한다")
    void refresh_fail_redis() {
        // given
        String refreshToken = "refreshTokenValue";
        when(refreshTokenRepository.findById(refreshToken)).thenReturn(Optional.empty());

        // when & then
        assertThrows(AuthException.ExpiredTokenException.class, () -> authService.validateTokenFromRedis(refreshToken));

    }
}