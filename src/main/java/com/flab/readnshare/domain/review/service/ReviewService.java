package com.flab.readnshare.domain.review.service;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import com.flab.readnshare.global.common.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException.ReviewNotFoundException::new);
    }

    public Long save(Review review) {
        return reviewRepository.save(review).getId();
    }

    @Transactional
    public Long update(Long reviewId, Member signInMember, UpdateReviewRequestDto dto) {
        Review review = findById(reviewId);
        review.verifyMember(signInMember);

        review.update(dto.getContent());

        return review.getId();
    }

    @Transactional
    public void delete(Long reviewId, Member signInMember) {
        Review review = findById(reviewId);
        review.verifyMember(signInMember);

        reviewRepository.delete(review);
    }
}
