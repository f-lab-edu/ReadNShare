package com.flab.readnshare.domain.auth.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class InvalidPasswordException extends BaseDomainException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}
