package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlannerRequest {
    private Long plannerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String plannerName;
}

