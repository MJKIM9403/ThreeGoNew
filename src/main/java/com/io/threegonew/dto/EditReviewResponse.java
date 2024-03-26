package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditReviewResponse {
    private Long reviewId;
    private Long reviewBookId;
    private String reviewBookTitle;
    private String tourItemId;
    private String tourItemTitle;
    private String reviewContent;
    private List<ReviewPhotoResponse> reviewPhotoList;
}
