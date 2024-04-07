package com.flab.readnshare;

import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.github.javafaker.Faker;

public class ReviewTestFixture {
    public static final Faker faker = Faker.instance();

    private static BookDto bookDto = BookDto.builder()
            .id(1L)
            .isbn(faker.number().toString())
            .title(faker.book().title())
            .build();

    public static SaveReviewRequestDto getSaveReviewRequestDto() {
        return SaveReviewRequestDto.builder()
                .book(bookDto)
                .content("내용")
                .build();
    }

    public static UpdateReviewRequestDto getUpdateReviewRequestDto() {
        return UpdateReviewRequestDto.builder()
                .content("수정 내용")
                .build();
    }

    public static Review getReviewEntity() {
        return Review.builder()
                .id(1L)
                .content("내용")
                .book(bookDto.toEntity())
                .member(getMemberEntity())
                .build();
    }

    public static Member getMemberEntity() {
        return Member.builder()
                .id(1L)
                .email(faker.internet().emailAddress())
                .password("test24680!")
                .build();
    }

}

