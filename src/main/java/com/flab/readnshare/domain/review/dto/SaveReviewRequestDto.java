package com.flab.readnshare.domain.review.dto;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SaveReviewRequestDto {
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    @Valid
    private BookDto book;

    @Builder
    public SaveReviewRequestDto(String content, BookDto book) {
        this.content = content;
        this.book = book;
    }

    public Review toEntity(Member member, Book book) {
        return Review.builder()
                .member(member)
                .book(book)
                .content(content)
                .build();
    }
}
