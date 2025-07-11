package com.flab.readnshare.domain.follow.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class DuplicateFollowException extends BaseDomainException {
    public DuplicateFollowException() {
        super(ErrorCode.FOLLOW_DUPLICATION);
    }
}
