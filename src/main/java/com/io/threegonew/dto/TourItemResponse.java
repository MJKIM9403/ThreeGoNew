package com.io.threegonew.dto;

import com.io.threegonew.domain.TourItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourItemResponse {
    private String contentid;
    private String cat1;
    private String cat2;
    private String cat3;
    private String fullCategoryName;
    private String areacode;
    private String contenttypeid;
    private String address;
    private String firstimage;
    private String mapx;
    private String mapy;
    private String mlevel;
    private String sigungucode;
    private String tel;
    private String title;

}
