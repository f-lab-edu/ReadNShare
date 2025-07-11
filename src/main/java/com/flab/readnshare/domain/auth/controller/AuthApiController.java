package com.flab.readnshare.domain.auth.controller;

import com.flab.readnshare.domain.auth.dto.SignInRequestDto;
import com.flab.readnshare.domain.auth.exception.DeniedTokenException;
import com.flab.readnshare.domain.auth.exception.ExpiredTokenException;
import com.flab.readnshare.domain.auth.exception.NullTokenException;
import com.flab.readnshare.domain.auth.service.AuthService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.dto.MemberResponseDto;
import com.flab.readnshare.global.common.auth.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signIn")
    public ResponseEntity<MemberResponseDto> signIn(@RequestBody SignInRequestDto dto, HttpServletResponse response) {
        Member member = authService.signIn(dto);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .build();

        // access token 발급
        authService.sendAccessToken(response, member.getId());
        // refresh token 발급
        authService.sendRefreshToken(response, member.getId());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(value = "refreshToken") Cookie cookie, HttpServletResponse response) {
        String refreshToken = Optional.ofNullable(cookie)
                .map(Cookie::getValue)
                .orElseThrow(NullTokenException::new);

        String memberId = Optional.ofNullable(refreshToken)
                .map(jwtUtil::extractMemberId)
                .orElseThrow(DeniedTokenException::new);

        switch (jwtUtil.validateToken(refreshToken)) {
            case DENIED -> throw new DeniedTokenException();
            case EXPIRED -> throw new ExpiredTokenException();
            case ACCESS -> {
                authService.validateTokenFromRedis(refreshToken);
                authService.sendAccessToken(response, Long.valueOf(memberId));

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
