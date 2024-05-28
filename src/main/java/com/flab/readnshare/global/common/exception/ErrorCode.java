package com.flab.readnshare.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_PARAMETER(HttpStatus.BAD_REQUEST, "입력값을 확인하세요."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템에 문제가 발생했습니다."),

    // Member
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

    // Auth
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    JWT_DENIED(HttpStatus.UNAUTHORIZED, "조작되거나 지원되지 않는 토큰입니다."),
    JWT_NULL(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 독서 기록 입니다."),
    REVIEW_FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "해당 독서 기록에 대한 수정 및 삭제 권한이 없습니다."),

    // Follow
    SELF_FOLLOW(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우 할 수 없습니다."),
    FOLLOW_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 해당 사용자를 팔로우하고 있습니다.");

    private final HttpStatus status;
    private final String message;
}
