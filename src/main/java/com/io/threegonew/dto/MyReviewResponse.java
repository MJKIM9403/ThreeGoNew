package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewResponse {
    private Long reviewId;
    private ReviewPhotoResponse firstPhoto;
    private Integer photoCount;
    private Integer likeCount;
    private Integer commentCount;
}
