package com.flab.readnshare.global.common.exception;

import lombok.Getter;

@Getter
public class NotificationException extends BaseDomainException {
    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class FCMTokenNotFoundException extends NotificationException {
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

    public static class InvalidFCMTokenException extends NotificationException {
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
}
