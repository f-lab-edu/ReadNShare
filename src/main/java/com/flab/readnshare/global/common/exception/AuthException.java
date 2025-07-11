package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class AuthException extends BaseDomainException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class InvalidPasswordException extends AuthException {
        public InvalidPasswordException() {
            super(ErrorCode.INVALID_PASSWORD);
        }
    }

    public static class ExpiredTokenException extends AuthException {
        public ExpiredTokenException() {
            super(ErrorCode.JWT_EXPIRED);
        }
    }

    public static class DeniedTokenException extends AuthException {
        public DeniedTokenException() {
            super(ErrorCode.JWT_DENIED);
        }
    }

    public static class NullTokenException extends AuthException {
        public NullTokenException() {
            super(ErrorCode.JWT_NULL);
        }
    }

}
