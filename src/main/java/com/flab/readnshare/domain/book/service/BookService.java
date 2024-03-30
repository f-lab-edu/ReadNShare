package com.flab.readnshare.domain.book.service;

import com.flab.readnshare.domain.book.domain.Book;
import com.flab.readnshare.domain.book.dto.BookDto;
import com.flab.readnshare.domain.book.dto.SearchBookDetailReponseDto;
import com.flab.readnshare.domain.book.dto.SearchBookReponseDto;
import com.flab.readnshare.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    @Value("${naver.book.id}")
    private String clientId;

    @Value("${naver.book.secret}")
    private String clientSecret;

    private final String SEARCH_BOOK_URL = "https://openapi.naver.com/v1/search/book.json?display=10";
    private final String SEARCH_BOOK_DETAIL_URL = "https://openapi.naver.com/v1/search/book_adv.json";

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;

    /**
     * 책 검색
     */
    public SearchBookReponseDto searchBook(String keyword, int start) {
        HttpEntity<String> httpEntity = getHttpEntitiy();

        URI targetUrl = UriComponentsBuilder
                .fromUriString(SEARCH_BOOK_URL)
                .queryParam("query", keyword)
                .queryParam("start", start)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        return restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, SearchBookReponseDto.class).getBody();
    }

    /**
     * 책 상세 검색
     */
    public SearchBookDetailReponseDto searchBookDetail(String isbn) {
        HttpEntity<String> httpEntity = getHttpEntitiy();

        URI targetUrl = UriComponentsBuilder
                .fromUriString(SEARCH_BOOK_DETAIL_URL)
                .queryParam("d_isbn", isbn)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        return restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, SearchBookDetailReponseDto.class).getBody();
    }

    private HttpEntity<String> getHttpEntitiy() {
        // 헤더 인증 정보 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Naver-Client-Id", clientId);
        httpHeaders.set("X-Naver-Client-Secret", clientSecret);
        return new HttpEntity<>(httpHeaders);
    }

    /**
     * 책 등록
     */
    public Book save(BookDto dto) {
        Book book = bookRepository.findByIsbn(dto.getIsbn())
                .orElseGet(() -> bookRepository.save(dto.toEntity()));
        return book;
    }

}
