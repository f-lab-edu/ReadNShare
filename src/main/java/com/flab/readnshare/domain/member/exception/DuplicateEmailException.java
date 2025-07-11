package com.flab.readnshare.domain.member.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class DuplicateEmailException extends BaseDomainException {
    public DuplicateEmailException() {
        super(ErrorCode.EMAIL_DUPLICATION);
    }
}
