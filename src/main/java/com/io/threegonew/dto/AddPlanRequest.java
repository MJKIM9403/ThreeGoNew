package com.io.threegonew.dto;


import com.io.threegonew.domain.TourItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPlanRequest {
    private Long plannerId; // 플래너 ID
    private String userId; // 사용자 ID
    private Integer day; // 날짜
    private Integer order; // 순서
    private String contentId; // 컨텐츠 ID
}