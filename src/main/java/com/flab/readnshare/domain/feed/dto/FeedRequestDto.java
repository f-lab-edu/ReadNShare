package com.flab.readnshare.domain.feed.dto;

import lombok.Getter;

@Getter
public class FeedRequestDto {
    private Long lastReviewId;
    private Integer limit;
}
