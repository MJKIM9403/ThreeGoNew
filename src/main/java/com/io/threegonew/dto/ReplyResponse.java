package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponse {
    private Long commentId;
    private UserInfoResponse writer;
    private String content;
    private Integer group;
    private Long parentId;
    private String patentWriterId;
    private Boolean cmtDel;
}
