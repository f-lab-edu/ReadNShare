package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class FollowException extends BaseDomainException {
    public FollowException(ErrorCode errorCode) {
        super(errorCode);
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
