package com.flab.readnshare.domain.auth.dto;

import com.flab.readnshare.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInRequestDto {
    private String email;
    private String password;

    @Builder
    public SignInRequestDto(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }

}
