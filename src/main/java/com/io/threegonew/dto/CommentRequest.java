package com.io.threegonew.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    private Long reviewId;
    private Integer group;

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
