package com.flab.readnshare.domain.review.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class ForbiddenMemberException extends BaseDomainException {
    public ForbiddenMemberException() {
        super(ErrorCode.REVIEW_FORBIDDEN_MEMBER);
    }
}
