package com.io.threegonew.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MyPlannerResponse {
    private String plannerId;
    private String plannerName;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public MyPlannerResponse(String plannerId, String plannerName, LocalDate startDate, LocalDate endDate ) {
        this.plannerId = plannerId;
        this.plannerName = plannerName;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
