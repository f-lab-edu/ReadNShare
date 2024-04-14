package com.flab.readnshare.domain.review.service;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.book.service.BookService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import com.flab.readnshare.global.common.exception.MemberException;
import com.flab.readnshare.global.common.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@EnableRetry
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookService bookService;

    @Transactional(readOnly = true)
    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException.ReviewNotFoundException::new);
    }

    public Long save(SaveReviewRequestDto dto, Member signInMember) {
        BookDto bookDto = dto.getBook();
        Book book = bookService.save(bookDto);

        Review review = dto.toEntity(signInMember, book);

        return reviewRepository.save(review).getId();
    }

    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class}
            , maxAttempts = 3
            , backoff = @Backoff(delay = 1000)
    )
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

    public void delete(Long reviewId, Member signInMember) {
        Review review = findById(reviewId);
        review.verifyMember(signInMember);

        reviewRepository.delete(review);
    }
}
