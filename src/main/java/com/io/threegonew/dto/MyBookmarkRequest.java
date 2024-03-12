package com.io.threegonew.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyBookmarkRequest {
    private String userId;

    @Builder.Default
    @Min(value = 0)
    @Positive
    private int page = 0;

    @Builder.Default
    @Min(value = 12)
    @Max(value = 100)
    @Positive
    private int size = 12;
}
