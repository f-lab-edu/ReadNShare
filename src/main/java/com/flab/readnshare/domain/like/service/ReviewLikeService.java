package com.flab.readnshare.domain.like.service;

import com.flab.readnshare.domain.like.domain.ReviewLike;
import com.flab.readnshare.domain.like.domain.ReviewLikeCount;
import com.flab.readnshare.domain.like.repository.ReviewLikeCountRepository;
import com.flab.readnshare.domain.like.repository.ReviewLikeRepository;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.member.exception.MemberNotFoundException;
import com.flab.readnshare.domain.member.repository.MemberRepository;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.exception.ReviewNotFoundException;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ReviewLikeCountRepository reviewLikeCountRepository;

    public Long count(Long reviewId) {
        return reviewLikeCountRepository.findById(reviewId)
                .map(ReviewLikeCount::getCount)
                .orElse(0L);
    }

    @Transactional
    public void like(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        reviewLikeRepository.save(
                ReviewLike.create(review, member)
        );

        int result = reviewLikeCountRepository.increase(reviewId);
        if (result == 0) {
            reviewLikeCountRepository.save(ReviewLikeCount.create(review, 1L));
        }
    }

    @Transactional
    public void unlike(Long reviewId, Long memberId) {
        reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId)
                .ifPresent(entity -> {
                    reviewLikeRepository.delete(entity);
                    reviewLikeCountRepository.decrease(reviewId);
                });
    }
}
