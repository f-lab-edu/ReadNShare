package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class ReviewException extends BaseDomainException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class ReviewNotFoundException extends ReviewException {
        public ReviewNotFoundException() {
            super(ErrorCode.REVIEW_NOT_FOUND);
        }
    }

    public static class ForbiddenMemberException extends ReviewException {
        public ForbiddenMemberException() {
            super(ErrorCode.REVIEW_FORBIDDEN_MEMBER);
        }
    }

}
