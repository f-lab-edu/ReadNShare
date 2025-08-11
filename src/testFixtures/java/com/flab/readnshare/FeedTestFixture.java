package com.flab.readnshare;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;

import java.util.*;

public class FeedTestFixture {
    public static List<Long> getFollowerTestIds() {
        return Arrays.asList(1L, 2L, 3L);
    }

    public static Set<Object> getFeedSet() {
        return new LinkedHashSet<>(Arrays.asList("2", "1"));
    }

    public static List<Review> getReviews(Set<Object> feedSet) {
        return feedSet.stream()
                .map(reviewId -> getReview(Long.parseLong((String) reviewId)))
                .toList();
    }

    public static List<Long> getReviewIds(Set<Object> feedSet) {
        return feedSet.stream()
                .map(id -> Long.parseLong((String) id))
                .toList();
    }

    private static Review getReview(long id) {
        return Review.builder()
                .id(id)
                .content("content" + id)
                .book(Book.builder().title("test").build())
                .member(Member.builder().nickName("testNickName").build())
                .build();
    }

    public static List<FeedResponseDto> getFeedResponses() {
        return Arrays.asList(
                FeedResponseDto.of(1L, "user1", "content1", "book1"),
                FeedResponseDto.of(2L, "user1", "content2", "book2")
        );
    }
}
