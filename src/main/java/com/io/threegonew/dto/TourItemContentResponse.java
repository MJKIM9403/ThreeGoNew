package com.io.threegonew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourItemContentResponse {
    private TourItemResponse tourItemResponse;
    private List<String> imagesURL;
    private String overview;
    private Map<String, String> detailInfo;
    private List<MoreTourItemDTO> moreTourItems;
    private Long bookmarkCount;
}
