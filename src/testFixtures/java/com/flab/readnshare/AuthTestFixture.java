package com.flab.readnshare;

import com.flab.readnshare.domain.auth.dto.SignInRequestDto;
import com.github.javafaker.Faker;

public class AuthTestFixture {
    public static final Faker faker = Faker.instance();

    public static SignInRequestDto getSignInRequestDto(){
        return SignInRequestDto.builder()
                .email(faker.internet().emailAddress())
                .password("test24680!")
                .build();
    }

}

