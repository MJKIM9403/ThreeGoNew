package com.io.threegonew.dto;

import com.io.threegonew.domain.Planner;
import lombok.Getter;
import java.util.Date;

@Getter
public class CalendarResponse {
    private Date startDate;
    private Date endDate;

    public CalendarResponse (Planner planner) {
        this.startDate = getStartDate();
        this.endDate = getEndDate();
    }
}
