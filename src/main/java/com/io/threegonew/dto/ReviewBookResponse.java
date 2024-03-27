package com.io.threegonew.dto;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewBookResponse {
    private Long bookId;
    private UserInfoResponse user;
    private PlannerResponse planner;
    private String bookTitle;
    private String bookContent;
    private String coverOfile;
    private String coverFilePath;
}
