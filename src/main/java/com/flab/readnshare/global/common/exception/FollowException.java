package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class FollowException extends RuntimeException {
    private final ErrorCode errorCode;

    public FollowException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class DuplicateFollowException extends FollowException {
        public DuplicateFollowException() {
            super(ErrorCode.FOLLOW_DUPLICATION);
        }
    }

    public static class SelfFollowException extends FollowException {
        public SelfFollowException() {
            super(ErrorCode.SELF_FOLLOW);
        }
    }


}
