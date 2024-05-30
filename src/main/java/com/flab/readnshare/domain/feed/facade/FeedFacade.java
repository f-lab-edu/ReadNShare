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
import java.util.stream.Collectors;

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

        Set<Object> feedSet;
        if (lastReviewId == null) {
            feedSet = feedRedisTemplate.opsForZSet().reverseRange(userFeedKey, 0, (limit - 1));
        } else {
            Double score = feedRedisTemplate.opsForZSet().score(userFeedKey, String.valueOf(lastReviewId));

            // 마지막 리뷰의 score 이전 데이터들을 역순으로 limit 만큼 조회
            feedSet = feedRedisTemplate.opsForZSet().reverseRangeByScore(userFeedKey, Double.MIN_VALUE, (score - 1), 0, limit);
        }

        return Optional.ofNullable(feedSet)
                .orElse(Collections.emptySet())
                .stream()
                .map(reviewId -> {
                    Review review = reviewService.findById(Long.parseLong((String) reviewId));

                    return FeedResponseDto.builder()
                            .reviewId(review.getId())
                            .nickName(review.getMember().getNickName())
                            .content(review.getContent())
                            .bookTitle(review.getBook().getTitle())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
