package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoreTourItemDTO {
    private String contentid;
    private String title;
    private String firstimage;
    private Double distance;
    private String mapx;
    private String mapy;
}
