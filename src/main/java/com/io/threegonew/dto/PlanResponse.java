package com.io.threegonew.dto;

import com.io.threegonew.domain.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanResponse {
    private boolean success;
    private List<Plan> plans;
}
