package com.flab.readnshare.domain.auth.service;

import com.flab.readnshare.domain.auth.dto.SignInRequestDto;
import com.flab.readnshare.domain.auth.repository.RefreshTokenRepository;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.global.common.auth.jwt.JwtUtil;
import com.flab.readnshare.global.common.exception.AuthException;
import com.flab.readnshare.global.common.exception.MemberException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    /**
     * 로그인
     */
    public Member signIn(SignInRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(MemberException.MemberNotFoundException::new);

        if (member.getPassword().equals(dto.getPassword())) {
            return member;
        }
        throw new AuthException.InvalidPasswordException();
    }

    public void sendAccessToken(HttpServletResponse response, Long memberId) {
        String accessToken = jwtUtil.createAccessToken(memberId);
        jwtUtil.setAccessTokenHeader(response, accessToken);
    }

    public void sendRefreshToken(HttpServletResponse response, Long memberId) {
        String refreshToken = jwtUtil.createRefreshToken(memberId);
        jwtUtil.setRefreshTokenCookie(response, refreshToken);
    }

    public void validateTokenFromRedis(String refreshToken) {
        if (refreshTokenRepository.findById(refreshToken).isEmpty()) {
            throw new AuthException.ExpiredTokenException();
        }
    }

}
