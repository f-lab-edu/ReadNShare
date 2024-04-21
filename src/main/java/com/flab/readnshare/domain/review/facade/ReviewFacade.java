package com.flab.readnshare.domain.review.facade;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.book.service.BookService;
import com.flab.readnshare.domain.member.domain.Member;
import com.flab.readnshare.domain.review.domain.Review;
import com.flab.readnshare.domain.review.dto.SaveReviewRequestDto;
import com.flab.readnshare.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewFacade {
    private final BookService bookService;
    private final ReviewService reviewService;

    @Transactional
    public Long save(SaveReviewRequestDto dto, Member signInMember) {
        BookDto bookDto = dto.getBook();
        Book book = bookService.save(bookDto);

        Review review = dto.toEntity(signInMember, book);

        return reviewService.save(review);
    }
}
