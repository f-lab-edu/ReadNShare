package com.flab.readnshare.domain.like.service;

import com.flab.readnshare.domain.like.domain.ReviewLikeCount;
import com.flab.readnshare.domain.like.repository.ReviewLikeCountRepository;
import com.flab.readnshare.domain.like.repository.ReviewLikeRepository;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import com.flab.readnshare.support.IntegrationTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewLikeServiceConcurrencyTest extends IntegrationTestSupport {
    @Autowired
    private ReviewLikeService reviewLikeService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewLikeCountRepository reviewLikeCountRepository;
    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    private Review review;
    private final List<Member> members = new ArrayList<>();

    @BeforeEach
    void setUp() {
        review = reviewRepository.save(Review.builder().content("테스트 리뷰").build());
        reviewLikeCountRepository.save(ReviewLikeCount.create(review, 0L));

        for (int i = 0; i < 100; i++) {
            members.add(memberRepository.save(Member.builder().email("member" + i + "@test.com").build()));
        }
    }

    @AfterEach
    void tearDown() {
        reviewLikeRepository.deleteAllInBatch();
        reviewLikeCountRepository.deleteAllInBatch();
        reviewRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("동시에 100명이 좋아요를 눌렀을 때 카운트가 100이 된다.")
    @Test
    void like_concurrency_test() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int memberIndex = i;
            executorService.submit(() -> {
                try {
                    reviewLikeService.like(review.getId(), members.get(memberIndex).getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Long count = reviewLikeService.count(review.getId());
        assertThat(count).isEqualTo(threadCount);
    }
}
