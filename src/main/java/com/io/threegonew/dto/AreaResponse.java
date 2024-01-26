package com.io.threegonew.dto;

import com.io.threegonew.domain.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaResponse {
    private Integer areaCode;
    private String areaName;

    public AreaResponse(Area area) {
        this.areaCode = area.getAreaCode();
        this.areaName = area.getAreaName();
    }
}
