package com.flab.readnshare.domain.auth.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class DeniedTokenException extends BaseDomainException {
    public DeniedTokenException() {
        super(ErrorCode.JWT_DENIED);
    }
}
