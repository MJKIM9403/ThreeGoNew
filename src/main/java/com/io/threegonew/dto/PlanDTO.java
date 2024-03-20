package com.io.threegonew.dto;

import com.io.threegonew.domain.TourItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO<E> {
    private Long planId;
    private Long plannerId;
    private Integer day;
    private Integer order;
    private List<E> dtoList;

    public void setDtoList(List<E> dtoList) {
        this.dtoList = dtoList;
    }
}
