package com.flab.readnshare.domain.feed.facade;

import com.flab.readnshare.FeedTestFixture;
import com.flab.readnshare.ReviewTestFixture;
import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedFacadeTest {
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ReviewService reviewService;
    @Mock
    private ZSetOperations<String, Object> zSetOperations;
    @InjectMocks
    private FeedFacade feedFacade;

    @Test
    @DisplayName("피드 데이터를 생성한다.")
    void add_to_feed_success() {
        // given
        List<Long> followerIds = FeedTestFixture.getFollowerTestIds();
        Review review = ReviewTestFixture.getReviewEntity();

        // when
        feedFacade.addToFeed(followerIds, review);

        // then
        verify(redisTemplate, times(1)).executePipelined(any(RedisCallback.class));
    }

    @DisplayName("lastReviewId가 없을 때(첫 페이지 로딩) 피드를 성공적으로 조회한다.")
    @Test
    void get_feed_initial_load_success() {
        // given
        Long memberId = 1L;
        int limit = 5;
        String userFeedKey = String.format("user:%d:feed", memberId);
        Set<Object> feedSet = FeedTestFixture.getFeedSet();

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRange(anyString(), anyLong(), anyLong())).thenReturn(feedSet);

        List<Review> reviews = FeedTestFixture.getReviews(feedSet);
        List<Long> reviewIds = FeedTestFixture.getReviewIds(feedSet);
        when(reviewService.findByIdIn(reviewIds)).thenReturn(reviews);

        // when
        List<FeedResponseDto> result = feedFacade.getFeed(memberId, null, limit);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("reviewId").containsExactly(2L, 1L);
        assertThat(result).extracting("content").containsExactly("content2", "content1");

        verify(zSetOperations).reverseRange(userFeedKey, 0, (limit - 1));
        verify(reviewService).findByIdIn(reviewIds);
    }

    @DisplayName("lastReviewId가 있을 때 다음 피드를 성공적으로 조회한다.")
    @Test
    void get_feed_next_load_success() {
        // given
        Long memberId = 1L;
        Long lastReviewId = 5L;
        int limit = 5;
        String userFeedKey = String.format("user:%d:feed", memberId);
        double lastReviewScore = 10.0;

        Set<Object> feedSet = FeedTestFixture.getFeedSet();

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.score(userFeedKey, String.valueOf(lastReviewId))).thenReturn(lastReviewScore);
        when(zSetOperations.reverseRangeByScore(userFeedKey, Double.MIN_VALUE, (lastReviewScore - 1), 0, limit))
                .thenReturn(feedSet);

        List<Review> reviews = FeedTestFixture.getReviews(feedSet);
        List<Long> reviewIds = FeedTestFixture.getReviewIds(feedSet);
        when(reviewService.findByIdIn(reviewIds)).thenReturn(reviews);

        // when
        List<FeedResponseDto> result = feedFacade.getFeed(memberId, lastReviewId, limit);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("reviewId").containsExactly(2L, 1L);
        assertThat(result).extracting("content").containsExactly("content2", "content1");

        verify(zSetOperations).score(userFeedKey, String.valueOf(lastReviewId));
        verify(zSetOperations).reverseRangeByScore(userFeedKey, Double.MIN_VALUE, (lastReviewScore - 1), 0, limit);
        verify(reviewService).findByIdIn(reviewIds);
    }

    @DisplayName("Redis에서 조회된 피드가 없을 경우 빈 리스트를 반환한다.")
    @Test
    void get_feed_when_redis_returns_empty_should_return_empty_list() {
        // given
        Long memberId = 1L;
        int limit = 5;
        String userFeedKey = String.format("user:%d:feed", memberId);

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRange(userFeedKey, 0, (limit - 1))).thenReturn(Collections.emptySet());

        // when
        List<FeedResponseDto> result = feedFacade.getFeed(memberId, null, limit);

        // then
        assertThat(result).isEmpty();
        verify(reviewService, never()).findByIdIn(anyList());
    }

    @DisplayName("score 조회가 실패하면 빈 리스트를 반환한다")
    @Test
    void get_feed_when_score_null_should_return_empty_list() {
        // given
        Long memberId = 1L;
        Long lastReviewId = 5L;
        int limit = 5;
        String userFeedKey = String.format("user:%d:feed", memberId);

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.score(userFeedKey, String.valueOf(lastReviewId))).thenReturn(null);

        // when
        List<FeedResponseDto> result = feedFacade.getFeed(memberId, lastReviewId, limit);

        // then
        assertThat(result).isEmpty();

        verify(zSetOperations, never()).reverseRangeByScore(anyString(), anyDouble(), anyDouble(), anyLong(), anyLong());
        verify(reviewService, never()).findByIdIn(anyList());
    }

}