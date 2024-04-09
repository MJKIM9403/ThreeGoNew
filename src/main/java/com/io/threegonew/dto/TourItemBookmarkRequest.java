package com.io.threegonew.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourItemBookmarkRequest {
    // 유저 아이디
    private String userId;

    // 키워드 추가
    private String keyword;

    private String cat1;
    private String cat2;
    private String cat3;

    private String areaCode;
    private String sigunguCode;

    private String contentTypeId;

    // plannerId 추가함
    private Long plannerId;

    @Builder.Default
    @Min(value = 0)
    @Positive
    private int page = 0;

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10;
}
