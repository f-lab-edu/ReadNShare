package com.flab.readnshare.domain.like.repository;

import com.flab.readnshare.domain.like.domain.ReviewLikeCount;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import com.flab.readnshare.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ReviewLikeCountRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private ReviewLikeCountRepository reviewLikeCountRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private EntityManager em;

    @DisplayName("리뷰 좋아요 수를 1 증가시킨다.")
    @Test
    void increase_success() {
        // given
        Review review = reviewRepository.save(Review.builder().content("리뷰입니다.").build());
        reviewLikeCountRepository.save(ReviewLikeCount.create(review, 2L));

        // when
        int updatedRows = reviewLikeCountRepository.increase(review.getId());
        em.flush();
        em.clear();

        // then
        assertThat(updatedRows).isEqualTo(1);
        reviewLikeCountRepository.findById(review.getId())
                .ifPresent(count -> assertThat(count.getCount()).isEqualTo(3));
    }

    @DisplayName("리뷰 좋아요 수를 1 감소시킨다.")
    @Test
    void decrease_success() {
        // given
        Review review = reviewRepository.save(Review.builder().content("리뷰입니다.").build());
        reviewLikeCountRepository.save(ReviewLikeCount.create(review, 2L));

        // when
        int updatedRows = reviewLikeCountRepository.decrease(review.getId());
        em.flush();
        em.clear();

        // then
        assertThat(updatedRows).isEqualTo(1);
        reviewLikeCountRepository.findById(review.getId())
                .ifPresent(count -> assertThat(count.getCount()).isEqualTo(1));
    }

}