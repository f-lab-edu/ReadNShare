package com.flab.readnshare.domain.book.repository;

import com.flab.readnshare.domain.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}
