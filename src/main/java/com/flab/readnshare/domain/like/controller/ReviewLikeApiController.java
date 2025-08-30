package com.flab.readnshare.domain.like.controller;

import com.flab.readnshare.domain.like.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewLikeApiController {
    private final ReviewLikeService reviewLikeService;

    @PostMapping("/api/review-like/review/{reviewId}/member/{memberId}")
    public ResponseEntity<Void> likeReview(@PathVariable Long reviewId, @PathVariable Long memberId) {
        reviewLikeService.like(reviewId, memberId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/review-like/review/{reviewId}/member/{memberId}")
    public ResponseEntity<Void> unlikeReview(@PathVariable Long reviewId, @PathVariable Long memberId) {
        reviewLikeService.unlike(reviewId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
