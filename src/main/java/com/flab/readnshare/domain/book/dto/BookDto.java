package com.flab.readnshare.domain.book.dto;

import com.flab.readnshare.domain.book.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookDto {

    private Long id;
    private String title;
    private String isbn;
    private String image;
    private String author;
    private String publisher;
    private String link;
    private String description;
    
    @Builder
    public BookDto(Long id, String title, String isbn, String image, String author, String publisher, String link, String description){
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.image = image;
        this.author = author;
        this.publisher = publisher;
        this.link = link;
        this.description = description;
    }

    public Book toEntity(){
        return Book.builder()
                .title(title)
                .isbn(isbn)
                .image(image)
                .author(author)
                .publisher(publisher)
                .link(link)
                .description(description)
                .build();
    }
}
