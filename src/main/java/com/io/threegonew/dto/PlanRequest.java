package com.io.threegonew.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequest {
    private Long plannerId;
    private Integer day;
}
