package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class DuplicateEmailException extends MemberException {
        public DuplicateEmailException() {
            super(ErrorCode.EMAIL_DUPLICATION);
        }
    }

}
