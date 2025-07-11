package com.flab.readnshare.global.common.exception;

public abstract class BaseDomainException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BaseDomainException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
