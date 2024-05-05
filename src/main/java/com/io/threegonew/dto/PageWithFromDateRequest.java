package com.io.threegonew.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageWithFromDateRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromdate;

    private String keyword;

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
