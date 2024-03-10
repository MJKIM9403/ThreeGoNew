package com.io.threegonew.dto;

import com.io.threegonew.domain.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkRequest {
    private String contentId;
    private String userId;
}
