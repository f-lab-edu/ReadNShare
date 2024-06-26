package com.flab.readnshare.domain.book.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchBookReponseDto {
    public Integer total;
    List<Items> items = new ArrayList<>();

    @Getter
    final static class Items {
        private String title;
        private String image;
        private String author;
        private String isbn;
        private String description;
    }
}
