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
public class SimpleReviewResponse {
    private Long reviewId;
    private UserInfoResponse userInfo;
    private String tourItemTitle;
    private String reviewContent;
    private List<ReviewPhotoResponse> reviewPhotoList;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private boolean likeState;
}
