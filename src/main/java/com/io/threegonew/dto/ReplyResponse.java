package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
