package com.flab.readnshare.domain.book.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchBookReponseDto {
    public Integer total;
    List<Items> items = new ArrayList<>();

    static class Items {
        public String title;
        public String image;
        public String author;
        public String isbn;
        public String description;
    }
}
