package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private UserInfoResponse writer;
    private String content;
    private Integer group;
    private Integer childrenCount;
    private Boolean cmtDel;
}
