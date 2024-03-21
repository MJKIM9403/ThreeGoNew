package com.io.threegonew.dto;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.ReviewPhoto;
import com.io.threegonew.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private Long reviewBookId;
    private String reviewBookTitle;
    private UserInfoResponse userInfo;
    private String tourItemId;
    private String tourItemTitle;
    private String reviewContent;
    private Long viewCount;
    private List<ReviewPhoto> reviewPhotoList;
}
