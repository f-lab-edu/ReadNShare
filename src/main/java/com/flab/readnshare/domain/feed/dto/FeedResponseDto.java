package com.flab.readnshare.domain.feed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedResponseDto {
    private Long reviewId;
    private String nickName;
    private String content;
    private String bookTitle;

    @Builder
    public FeedResponseDto(Long reviewId, String nickName, String content, String bookTitle) {
        this.reviewId = reviewId;
        this.nickName = nickName;
        this.content = content;
        this.bookTitle = bookTitle;
    }

    public static FeedResponseDto of(Long reviewId, String nickName, String content, String bookTitle){
        FeedResponseDto response = new FeedResponseDto();
        response.reviewId = reviewId;
        response.nickName = nickName;
        response.content = content;
        response.bookTitle = bookTitle;
        return response;
    }
}
