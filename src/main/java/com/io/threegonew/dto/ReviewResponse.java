package com.io.threegonew.dto;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.ReviewPhoto;
import com.io.threegonew.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private String reviewBookId;
    private String reviewBookTitle;
    private String reviewBookCoverImg;
    private UserInfoResponse userInfo;
    private String tourItemId;
    private String tourItemTitle;
    private String reviewContent;
    private Long viewCount;
    private List<ReviewPhotoResponse> reviewPhotoList;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
