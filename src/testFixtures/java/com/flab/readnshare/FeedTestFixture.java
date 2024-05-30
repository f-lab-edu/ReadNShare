package com.flab.readnshare;

import com.flab.readnshare.domain.review.domain.Review;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
