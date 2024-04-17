package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerResponse {
    private Long plannerId;
    private String userId;
    private String plannerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean plannerDelete;
}
