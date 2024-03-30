package com.flab.readnshare.domain.review.service;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.book.service.BookService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.dto.UpdateReviewRequestDto;
import com.flab.readnshare.domain.review.repository.ReviewRepository;
import com.flab.readnshare.global.common.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
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

    public Long update(Long reviewId, Member signInMember, UpdateReviewRequestDto dto) {
        Review review = findById(reviewId);
        review.verifyMember(signInMember);

        review.update(dto.getContent());

        return review.getId();
    }

    public void delete(Long reviewId, Member signInMember) {
        Review review = findById(reviewId);
        review.verifyMember(signInMember);

        reviewRepository.delete(review);
    }
}
