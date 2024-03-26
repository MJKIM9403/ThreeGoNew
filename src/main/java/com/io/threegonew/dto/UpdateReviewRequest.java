package com.io.threegonew.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class UpdateReviewRequest {
    private Long reviewId;
    private Long bookId;
    private String touritemId;
    private String touritemTitle;
    private String reviewContent;
    private List<MultipartFile> photoList;
    private List<Long> deletePhotoId;

    @Builder
    public UpdateReviewRequest(Long reviewId, Long bookId, String touritemId,
                            String touritemTitle, String reviewContent,
                            List<MultipartFile> photoList, List<Long> deletePhotoId)
    {
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.touritemId = touritemId;
        this.touritemTitle = touritemTitle;
        this.reviewContent = reviewContent;
        this.photoList = photoList;
        this.deletePhotoId = deletePhotoId;
    }
}
