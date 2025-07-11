package com.flab.readnshare.domain.member.exception;

import com.flab.readnshare.global.common.exception.BaseDomainException;
import com.flab.readnshare.global.common.exception.ErrorCode;

public class MemberNotFoundException extends BaseDomainException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
