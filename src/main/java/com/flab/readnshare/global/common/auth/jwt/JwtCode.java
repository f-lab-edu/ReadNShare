package com.flab.readnshare.global.common.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtCode {
    EXPIRED,
    ACCESS,
    DENIED,
}
