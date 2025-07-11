package com.flab.readnshare.domain.auth.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class ExpiredTokenException extends BaseDomainException {
    public ExpiredTokenException() {
        super(ErrorCode.JWT_EXPIRED);
    }
}
