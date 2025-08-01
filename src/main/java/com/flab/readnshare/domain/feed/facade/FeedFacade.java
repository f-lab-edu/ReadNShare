package com.flab.readnshare.domain.feed.facade;

import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FeedFacade {
    private final RedisTemplate<String, Object> feedRedisTemplate;
    private final ReviewService reviewService;

    private static final String KEY = "user:%d:feed";
    private static final long FEED_EXPIRE_DURATION = 7;

    public void addToFeed(List<Long> followerIds, Review review) {
        Long timestamp = System.currentTimeMillis();
        Long reviewId = review.getId();

        feedRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Long followerId : followerIds) {
                String userFeedKey = String.format(KEY, followerId);
                BoundZSetOperations<String, Object> operations = feedRedisTemplate.boundZSetOps(userFeedKey);
                operations.add(String.valueOf(reviewId), timestamp);
                operations.expire(FEED_EXPIRE_DURATION, TimeUnit.DAYS);
            }

            return null;
        });
    }

    public List<FeedResponseDto> getFeed(Long memberId, Long lastReviewId, int limit) {
        String userFeedKey = String.format(KEY, memberId);
        Set<Object> feedSet = fetchFeedFromRedis(userFeedKey, lastReviewId, limit);

        if (feedSet == null || feedSet.isEmpty()) {
            return Collections.emptyList();
        }

        return extractFeedResponses(feedSet);
    }

    private Set<Object> fetchFeedFromRedis(String userFeedKey, Long lastReviewId, int limit) {
        if (lastReviewId == null) {
            return feedRedisTemplate.opsForZSet().reverseRange(userFeedKey, 0, (limit - 1));
        }

        Double score = feedRedisTemplate.opsForZSet().score(userFeedKey, String.valueOf(lastReviewId));
        if (score == null) {
            return Collections.emptySet();
        }

        // 마지막 리뷰의 score 이전 데이터들을 역순으로 limit 만큼 조회
        return feedRedisTemplate.opsForZSet().reverseRangeByScore(userFeedKey, Double.MIN_VALUE, (score - 1), 0, limit);
    }

    private List<FeedResponseDto> extractFeedResponses(Set<Object> feedSet) {
        // 리뷰 ID 리스트 추출
        List<Long> reviewIds = Optional.ofNullable(feedSet)
                .orElse(Collections.emptySet())
                .stream()
                .map(reviewId -> Long.parseLong((String) reviewId))
                .toList();

        // 리뷰 ID 리스트를 IN 절로 사용하여 리뷰들을 한 번에 조회
        List<Review> reviews = reviewService.findByIdIn(reviewIds);

        return reviews.stream()
                .map(review -> FeedResponseDto.builder()
                        .reviewId(review.getId())
                        .nickName(review.getMember().getNickName())
                        .content(review.getContent())
                        .bookTitle(review.getBook().getTitle())
                        .build())
                .toList();
    }
}
