package com.flab.readnshare;

import com.flab.readnshare.domain.follow.domain.Follow;
import com.flab.readnshare.domain.member.domain.Member;
import com.github.javafaker.Faker;

public class FollowTestFixture {
    public static final Faker faker = Faker.instance();

    public static String getMemberEmail() {
        return faker.internet().emailAddress();
    }

    public static Member getMemberEntity() {
        return Member.builder()
                .id(1L)
                .email(faker.internet().emailAddress())
                .password("test24680!")
                .build();
    }

    public static Follow getFollowEntity(Member fromMember, Member toMember) {
        return Follow.builder()
                .id(1L)
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }

}

