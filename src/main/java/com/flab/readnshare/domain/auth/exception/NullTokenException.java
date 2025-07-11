package com.flab.readnshare.domain.auth.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class NullTokenException extends BaseDomainException {
    public NullTokenException() {
        super(ErrorCode.JWT_NULL);
    }
}
