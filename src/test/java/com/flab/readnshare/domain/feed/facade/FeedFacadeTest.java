package com.flab.readnshare.domain.feed.facade;

import com.flab.readnshare.FeedTestFixture;
import com.flab.readnshare.ReviewTestFixture;
import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;

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
        when(redisTemplate.executePipelined(any(RedisCallback.class))).thenReturn(null);

        feedFacade.addToFeed(followerIds, review);

        verify(redisTemplate, times(1)).executePipelined(any(RedisCallback.class));
    }

    @Test
    @DisplayName("피드를 최신 리뷰부터 조회한다.")
    void get_feed_latest_review() {
        // given
        Long memberId = 1L;
        int limit = 10;
        String userFeedKey = String.format("user:%d:feed", memberId);
        Set<Object> feedSet = FeedTestFixture.getFeedSet();

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        when(zSetOperations.reverseRange(anyString(), anyLong(), anyLong()))
                .thenReturn(feedSet);

        Review review = ReviewTestFixture.getReviewEntity();
        when(reviewService.findById(anyLong())).thenReturn(review);

        // when
        List<FeedResponseDto> feed = feedFacade.getFeed(memberId, null, limit);

        // then
        verify(zSetOperations).reverseRange(eq(userFeedKey), eq(0L), eq((long) limit - 1));
        verify(reviewService, times(feedSet.size())).findById(anyLong());

        assertEquals(feedSet.size(), feed.size());
        assertEquals(review.getId(), feed.get(0).getReviewId());
        assertEquals(review.getContent(), feed.get(0).getContent());
    }

    @Test
    @DisplayName("피드를 마지막 리뷰 다음부터 조회한다.")
    void get_feed_next_review() {
        // given
        Long memberId = 1L;
        Long lastReviewId = 10L;
        int limit = 5;
        String userFeedKey = String.format("user:%d:feed", memberId);
        Double lastReviewScore = 10.0;
        Set<Object> feedSet = FeedTestFixture.getFeedSet();

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.score(anyString(), eq(String.valueOf(lastReviewId)))).thenReturn(lastReviewScore);
        when(zSetOperations.reverseRangeByScore(anyString(), anyDouble(), anyDouble(), anyLong(), anyLong())).thenReturn(feedSet);

        Review review1 = createMockReview(1L, "좋은 리뷰입니다!", "이순신", "다른 책 제목");
        Review review2 = createMockReview(2L, "또 다른 리뷰!", "박지성", "다른 책 제목 2");

        when(reviewService.findById(1L)).thenReturn(review1);
        when(reviewService.findById(2L)).thenReturn(review2);

        // when
        List<FeedResponseDto> feed = feedFacade.getFeed(memberId, lastReviewId, limit);

        // then
        verify(zSetOperations).reverseRangeByScore(eq(userFeedKey), eq(Double.MIN_VALUE), eq(lastReviewScore - 1), eq(0L), eq((long) limit));
        verify(reviewService).findById(1L);
        verify(reviewService).findById(2L);

        assertEquals(2, feed.size());

        FeedResponseDto dto1 = feed.stream().filter(f -> f.getReviewId() == 1L).findFirst().orElse(null);
        assertEquals(1L, dto1.getReviewId());
        assertEquals("이순신", dto1.getNickName());
        assertEquals("좋은 리뷰입니다!", dto1.getContent());
        assertEquals("다른 책 제목", dto1.getBookTitle());

        FeedResponseDto dto2 = feed.stream().filter(f -> f.getReviewId() == 2L).findFirst().orElse(null);
        assertEquals(2L, dto2.getReviewId());
        assertEquals("박지성", dto2.getNickName());
        assertEquals("또 다른 리뷰!", dto2.getContent());
        assertEquals("다른 책 제목 2", dto2.getBookTitle());
    }

    private Review createMockReview(Long id, String content, String nickName, String bookTitle) {
        return Review.builder()
                .id(id)
                .content(content)
                .member(Member.builder().nickName(nickName).build())
                .book(Book.builder().title(bookTitle).build())
                .build();
    }
}