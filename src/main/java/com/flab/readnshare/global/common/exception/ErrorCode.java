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
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다.");

    private final HttpStatus status;
    private final String message;
}
