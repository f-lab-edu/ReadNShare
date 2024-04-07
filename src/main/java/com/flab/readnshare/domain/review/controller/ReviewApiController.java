package com.flab.readnshare.domain.review.controller;

import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.flab.readnshare.domain.review.service.ReviewService;
import com.flab.readnshare.global.common.resolver.SignInMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewApiController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody SaveReviewRequestDto dto, @SignInMember Member signInMember) {
        Long reviewId = reviewService.save(dto, signInMember);
        return new ResponseEntity<>(reviewId, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Long> update(
            @PathVariable Long reviewId
            , @SignInMember Member signInMember
            , @Valid @RequestBody UpdateReviewRequestDto dto) {
        return new ResponseEntity<>(reviewService.update(reviewId, signInMember, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable Long reviewId, @SignInMember Member signInMember) {
        reviewService.delete(reviewId, signInMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
