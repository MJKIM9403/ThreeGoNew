package com.io.threegonew.dto;

import com.io.threegonew.domain.Planner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class CalendarRequest {
    private Date startDate;
    private Date endDate;

    public Planner toEntity() {
        return Planner.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
