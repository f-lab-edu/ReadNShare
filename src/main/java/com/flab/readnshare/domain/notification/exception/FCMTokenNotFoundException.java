package com.flab.readnshare.domain.notification.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class FCMTokenNotFoundException extends BaseDomainException {
    private final Long memberId;

    public FCMTokenNotFoundException(Long memberId) {
        super(ErrorCode.FCM_TOKEN_NOT_FOUND);
        this.memberId = memberId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + String.format(" Member ID: %d", memberId);
    }
}
