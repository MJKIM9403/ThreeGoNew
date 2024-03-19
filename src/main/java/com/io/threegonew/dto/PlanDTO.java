package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private Long planId;
    private Long plannerId;
    private Integer day;
    private Integer order;
    private Long contentId;
    private String title;
    private String address;
}
