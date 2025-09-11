package com.flab.readnshare.domain.like.repository;

import com.flab.readnshare.domain.like.domain.ReviewLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeCountRepository extends JpaRepository<ReviewLikeCount, Long> {

    @Query(
            value = "update review_like_count set count = count + 1 where review_id = :reviewId",
            nativeQuery = true
    )
    @Modifying
    int increase(@Param("reviewId") Long reviewId);

    @Query(
            value = "update review_like_count set count = count - 1 where review_id = :reviewId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param("reviewId") Long reviewId);
}
