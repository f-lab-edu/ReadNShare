package com.flab.readnshare.domain.book.controller;

import com.flab.readnshare.domain.book.dto.SearchBookDetailReponseDto;
import com.flab.readnshare.domain.book.dto.SearchBookReponseDto;
import com.flab.readnshare.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookApiController {
    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<SearchBookReponseDto> searchBook(@RequestParam String keyword, @RequestParam int start){
        return new ResponseEntity<>(bookService.searchBook(keyword, start), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<SearchBookDetailReponseDto> searchBookDetail(@RequestParam String isbn){
        return new ResponseEntity<>(bookService.searchBookDetail(isbn), HttpStatus.OK);
    }
}
