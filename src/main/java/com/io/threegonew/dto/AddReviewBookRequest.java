package com.io.threegonew.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AddReviewBookRequest {
    private String userId;
    private Long plannerId;
    private String bookTitle;
    private String bookContent;
    private MultipartFile coverFile;

    @Builder
    public AddReviewBookRequest(String userId, Long plannerId, String bookTitle,
                                String bookContent, MultipartFile coverFile)
    {
        this.userId = userId;
        this.plannerId = plannerId;
        this.bookTitle = bookTitle;
        this.bookContent = bookContent;
        this.coverFile = coverFile;
    }
}
