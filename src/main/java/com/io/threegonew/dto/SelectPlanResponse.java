package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SelectPlanResponse {
    private Long planId;
    private Long plannerId;
    private Integer day;
    private Integer order;
    private TourItemResponse tourItem;
}
