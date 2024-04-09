package com.io.threegonew.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class AddCommentRequest {
    private Long reviewId;
    private Long parentId;
    private String content;

    @Builder
    public AddCommentRequest(Long reviewId, Long parentId, String content) {
        this.reviewId = reviewId;
        this.parentId = parentId;
        this.content = content;
    }
}
