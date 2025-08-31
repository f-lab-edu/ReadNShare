package com.flab.readnshare.domain.like.service;

import com.flab.readnshare.domain.like.domain.ReviewLike;
import com.flab.readnshare.domain.like.repository.ReviewLikeRepository;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.exception.MemberNotFoundException;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.exception.ReviewNotFoundException;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewLikeServiceTest {
    @Mock
    private ReviewLikeRepository reviewLikeRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ReviewLikeService reviewLikeService;

    @DisplayName("리뷰 좋아요 등록에 성공한다.")
    @Test
    void save_reviewLike_success() {
        // given
        Review review = Review.builder().content("내용").build();
        Member member = Member.builder().build();

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // when
        reviewLikeService.like(review.getId(), member.getId());

        // then
        verify(reviewLikeRepository, times(1)).save(any(ReviewLike.class));
    }

    @DisplayName("해당하는 reviewId가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void like_fail_review_not_found() {
        // given
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewLikeService.like(1L, 1L))
                .isInstanceOf(ReviewNotFoundException.class);

        verify(reviewLikeRepository, never()).save(any());
    }

    @DisplayName("해당하는 memberId가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void like_fail_member_not_found() {
        // given
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(Review.builder().id(1L).build()));
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewLikeService.like(1L, 1L))
                .isInstanceOf(MemberNotFoundException.class);

        verify(reviewLikeRepository, never()).save(any());
    }

    @DisplayName("리뷰 좋아요 삭제에 성공한다.")
    @Test
    void delete_reviewLike_success() {
        // given
        ReviewLike reviewLike = mock(ReviewLike.class);
        when(reviewLikeRepository.findByReviewIdAndMemberId(1L, 1L)).thenReturn(Optional.of(reviewLike));

        // when
        reviewLikeService.unlike(1L, 1L);

        // then
        verify(reviewLikeRepository, times(1)).delete(reviewLike);
    }
}