package com.flab.readnshare.domain.book.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchBookDetailReponseDto {
    List<Items> items = new ArrayList<>();

    final static class Items {
        private String title;
        private String image;
        private String author;
        private String isbn;
        private String description;
    }
}
