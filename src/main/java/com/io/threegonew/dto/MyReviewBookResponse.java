package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyReviewBookResponse {
    private String bookId;
    private String bookTitle;
    private String coverFilePath;

    @Builder
    public MyReviewBookResponse(String bookId, String bookTitle, String coverFilePath) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.coverFilePath = coverFilePath;
    }
}
