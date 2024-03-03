package com.flab.readnshare.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicateEmailException extends RuntimeException{
    private final ErrorCode errorCode;
}
