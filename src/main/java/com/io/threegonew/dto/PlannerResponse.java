package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerResponse {
    private Long plannerId;
    private String userId;
    private String plannerName;
    private Date startDate;
    private Date endDate;
}
