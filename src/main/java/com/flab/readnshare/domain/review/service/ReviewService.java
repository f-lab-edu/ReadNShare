package com.flab.readnshare.domain.review.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.flab.readnshare.domain.review.exception.ReviewNotFoundException;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableRetry
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }

    public Long save(Review review) {
        return reviewRepository.save(review).getId();
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class}
            , maxAttempts = 3
            , backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public Long update(Long reviewId, Member signInMember, UpdateReviewRequestDto dto) {
        Review review = reviewRepository.findByIdForUpdate(reviewId).orElseThrow();
        review.verifyMember(signInMember);

        review.update(dto.getContent());

        return review.getId();
    }

    @Recover
    public Long recover(ObjectOptimisticLockingFailureException ex, Long reviewId, Member signInMember, UpdateReviewRequestDto dto) {
        log.error("review update failed... error: {}", ex.getMessage());
        throw ex;
    }

    @Transactional
    public void delete(Long reviewId, Member signInMember) {
        Review review = findById(reviewId);
        review.verifyMember(signInMember);

        reviewRepository.delete(review);
    }

    public List<Review> findByIdIn(List<Long> reviewIds) {
        return reviewRepository.findByIdIn(reviewIds);
    }
}
