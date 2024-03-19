package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
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
        this.bookContent = bookContent.replace("\\r\\n", "<br>");
        this.coverFile = coverFile;
    }
}
