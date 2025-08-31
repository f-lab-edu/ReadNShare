package com.flab.readnshare.domain.like.repository;

import com.flab.readnshare.domain.like.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);
}
