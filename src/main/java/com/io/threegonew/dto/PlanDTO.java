package com.io.threegonew.dto;

import com.io.threegonew.domain.TourItem;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO<E> {
    private Long planId;
    private Long plannerId;
    private Integer day;
    private Integer order;
    private List<E> dtoList;
}
