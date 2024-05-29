package com.flab.readnshare.domain.feed.service;

import com.flab.readnshare.domain.feed.dto.FeedResponseDto;
import com.flab.readnshare.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final RedisTemplate<String, Object> feedRedisTemplate;

    private static final String KEY = "user:%d:feed";

    public void addToFeed(List<Long> followerIds, Review review) {
        Long timestamp = System.currentTimeMillis();

        FeedResponseDto dto = FeedResponseDto.builder()
                .nickName(review.getMember().getNickName())
                .content(review.getContent())
                .bookTitle(review.getBook().getTitle())
                .build();

        feedRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Long followerId : followerIds) {
                String userFeedKey = String.format(KEY, followerId);
                BoundZSetOperations<String, Object> operations = feedRedisTemplate.boundZSetOps(userFeedKey);
                operations.add(dto, timestamp);
            }

            return null;
        });
    }

    public List<FeedResponseDto> getFeed(Long memberId, int offset, int limit) {
        String userFeedKey = String.format(KEY, memberId);

        return Optional.ofNullable(feedRedisTemplate.opsForZSet()
                .reverseRange(userFeedKey, offset, limit))
                .orElse(Collections.emptySet())
                .stream()
                .map(FeedResponseDto.class::cast)
                .toList();
    }


}
