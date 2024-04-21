package com.flab.readnshare.domain.review.service;

import com.flab.readnshare.ReviewTestFixture;
import com.flab.readnshare.domain.book.service.BookService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import com.flab.readnshare.global.common.exception.ReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookService bookService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("해당 리뷰 id가 없는 경우 예외가 발생한다")
    void find_review_fail_no_id() {
        // given
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when & then
        assertThrows(ReviewException.ReviewNotFoundException.class, () -> reviewService.findById(1L));
    }

    @Test
    @DisplayName("독서 기록을 작성하면 review가 등록된다")
    void save_review_success() {
        // given
        SaveReviewRequestDto request = ReviewTestFixture.getSaveReviewRequestDto();
        Review expectedReview = ReviewTestFixture.getReviewEntity();

        when(reviewRepository.save(any(Review.class))).thenReturn(expectedReview);

        // when
        Long savedReviewId = reviewService.save(request, mock(Member.class));

        // then
        assertNotNull(savedReviewId);
        assertEquals(1L, savedReviewId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("독서 기록 내용을 수정하면 reviewId를 반환한다")
    void update_review_success() {
        // given
        UpdateReviewRequestDto request = ReviewTestFixture.getUpdateReviewRequestDto();
        Review existReview = ReviewTestFixture.getReviewEntity();

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(existReview));

        // when
        Long updatedReviewId = reviewService.update(existReview.getId(), existReview.getMember(), request);

        // then
        assertEquals(existReview.getId(), updatedReviewId);
        assertEquals(existReview.getContent(), request.getContent());
    }

    @Test
    @DisplayName("독서 기록 작성자와 수정자가 다르면 예외가 발생한다")
    void update_review_fail_mismatch_member() {
        // given
        UpdateReviewRequestDto request = ReviewTestFixture.getUpdateReviewRequestDto();
        Review existReview = ReviewTestFixture.getReviewEntity();

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(existReview));

        assertThrows(ReviewException.ForbiddenMemberException.class
                , () -> reviewService.update(existReview.getId(), mock(Member.class), request));
    }

    @Test
    @DisplayName("독서 기록 내용을 삭제한다")
    void delete_review_success() {
        // given
        Review existReview = ReviewTestFixture.getReviewEntity();

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(existReview));

        // when
        reviewService.delete(existReview.getId(), existReview.getMember());

        // then
        verify(reviewRepository, times(1)).delete(existReview);
    }

}