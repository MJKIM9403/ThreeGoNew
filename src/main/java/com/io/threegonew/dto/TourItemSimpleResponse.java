package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourItemSimpleResponse {
    private String contentid;
    private String fullCategoryName;
    private String address;
    private String firstimage;
    private String title;
}
