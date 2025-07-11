package com.flab.readnshare.domain.notification.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class InvalidFCMTokenException extends BaseDomainException {
    private final Long memberId;

    public InvalidFCMTokenException(Long memberId) {
        super(ErrorCode.INVALID_FCM_TOKEN);
        this.memberId = memberId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + String.format(" Member ID: %d", memberId);
    }
}
