package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigunguResponse {
    private Integer sigunguCode;
    private Integer areaCode;
    private String sigunguName;
}
