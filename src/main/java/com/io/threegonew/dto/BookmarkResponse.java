package com.io.threegonew.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponse {
    private Long bookmarkCount;
    private Boolean isChecked;
}
