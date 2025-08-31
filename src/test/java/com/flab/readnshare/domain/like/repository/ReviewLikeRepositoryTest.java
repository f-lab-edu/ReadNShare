package com.flab.readnshare.domain.like.repository;

import com.flab.readnshare.domain.like.domain.ReviewLike;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ReviewLikeRepositoryTest {
    @Autowired
    private ReviewLikeRepository reviewLikeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("reviewId와 memberId로 ReviewLike를 조회한다.")
    @Test
    void findByReviewIdAndMemberId_success() {
        // given
        Member member = memberRepository.save(Member.builder().build());
        Review review = reviewRepository.save(Review.builder().content("리뷰입니다.").build());

        reviewLikeRepository.save(
                ReviewLike.create(review, member)
        );

        // when
        Optional<ReviewLike> result = reviewLikeRepository.findByReviewIdAndMemberId(review.getId(), member.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getReview().getId()).isEqualTo(review.getId());
        assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
    }

    @DisplayName("같은 리뷰에 대해 같은 사용자의 중복 좋아요 시 예외가 발생한다.")
    @Test
    void like_fail_duplicate() {
        // given
        Member member = memberRepository.save(Member.builder().build());
        Review review = reviewRepository.save(Review.builder().content("리뷰입니다.").build());

        ReviewLike like1 = ReviewLike.create(review, member);
        reviewLikeRepository.save(like1);

        // when & then
        ReviewLike like2 = ReviewLike.create(review, member);

        assertThatThrownBy(() -> reviewLikeRepository.save(like2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}