package com.flab.readnshare.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveFCMTokenRequestDto {
    private String token;

    @Builder
    public SaveFCMTokenRequestDto(String token) {
        this.token = token;
    }
}
