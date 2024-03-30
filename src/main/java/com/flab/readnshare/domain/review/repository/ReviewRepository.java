package com.flab.readnshare.domain.review.repository;

import com.flab.readnshare.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
