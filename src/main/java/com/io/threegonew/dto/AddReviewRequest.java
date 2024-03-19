package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class AddReviewRequest {
    private Long bookId;
    private String userId;
    private String touritemId;
    private String touritemTitle;
    private String reviewContent;
    private List<MultipartFile> photoList;

    @Builder
    public AddReviewRequest(Long bookId, String userId, String touritemId,
                            String touritemTitle, String reviewContent,
                            List<MultipartFile> photoList)
    {
        this.bookId = bookId;
        this.userId = userId;
        this.touritemId = touritemId;
        this.touritemTitle = touritemTitle;
        this.reviewContent = reviewContent.replace("\\r\\n", "<br>");
        this.photoList = photoList;
    }
}
