package com.flab.readnshare.domain.like.controller;

import com.flab.readnshare.domain.like.service.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewLikeApiController {
    private final ReviewLikeService reviewLikeService;

    @GetMapping("/api/review-likes/reviews/{reviewId}/count")
    public ResponseEntity<Long> count(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewLikeService.count(reviewId), HttpStatus.OK);
    }

    @PostMapping("/api/review-likes/reviews/{reviewId}/members/{memberId}")
    public ResponseEntity<Void> likeReview(@PathVariable Long reviewId, @PathVariable Long memberId) {
        reviewLikeService.like(reviewId, memberId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/review-likes/reviews/{reviewId}/members/{memberId}")
    public ResponseEntity<Void> unlikeReview(@PathVariable Long reviewId, @PathVariable Long memberId) {
        reviewLikeService.unlike(reviewId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
