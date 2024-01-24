package com.io.threegonew.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourItemSelectRequest {
    private String cat1;
    private String cat2;
    private String cat3;

    private String areaCode;
    private String sigunguCode;

    private String contentTypeId;

}
