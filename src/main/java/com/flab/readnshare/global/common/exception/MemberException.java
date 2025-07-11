package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class MemberException extends BaseDomainException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class DuplicateEmailException extends MemberException {
        public DuplicateEmailException() {
            super(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    public static class MemberNotFoundException extends MemberException {
        public MemberNotFoundException() {
            super(ErrorCode.MEMBER_NOT_FOUND);
        }
    }


}
