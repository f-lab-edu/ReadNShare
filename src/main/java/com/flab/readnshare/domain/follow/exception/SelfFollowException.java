package com.flab.readnshare.domain.follow.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class SelfFollowException extends BaseDomainException {
    public SelfFollowException() {
        super(ErrorCode.SELF_FOLLOW);
    }
}
