package com.flab.readnshare.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_PARAMETER(HttpStatus.BAD_REQUEST, "입력값을 확인하세요."),

    // Member
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),

    // Auth
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다." ),
    JWT_DENIED(HttpStatus.UNAUTHORIZED, "조작되거나 지원되지 않는 토큰입니다."),
    JWT_NULL(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
