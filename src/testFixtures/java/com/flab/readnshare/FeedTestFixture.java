package com.flab.readnshare;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FeedTestFixture {
    public static List<Long> getFollowerTestIds() {
        return Arrays.asList(1L, 2L, 3L);
    }

    public static Set<Object> getFeedSet() {
        Set<Object> mockFeedSet = new HashSet<>();
        mockFeedSet.add("1");
        mockFeedSet.add("2");

        return mockFeedSet;
    }

    public static List<Review> getReviews(Set<Object> feedSet) {
        return feedSet.stream()
                .map(reviewId -> getReview(Long.parseLong((String) reviewId)))
                .collect(Collectors.toList());
    }

    private static Review getReview(long id) {
        return Review.builder()
                .id(id)
                .content("Content " + id)
                .book(Book.builder().title("test").build())
                .member(Member.builder().nickName("testNickName").build())
                .build();
    }
}
