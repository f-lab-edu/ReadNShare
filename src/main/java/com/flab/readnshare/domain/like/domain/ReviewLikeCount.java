package com.flab.readnshare.domain.like.domain;

import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLikeCount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_count_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private Long count;

    @Builder
    public ReviewLikeCount(Review review, Long count) {
        this.review = review;
        this.count = count;
    }

    public static ReviewLikeCount create(Review review) {
        return ReviewLikeCount.builder()
                .review(review)
                .count(0L)
                .build();
    }

    public void increase() {
        this.count++;
    }

    public void decrease() {
        this.count--;
    }
}
