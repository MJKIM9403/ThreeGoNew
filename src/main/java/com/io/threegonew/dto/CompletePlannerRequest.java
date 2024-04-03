package com.io.threegonew.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompletePlannerRequest {
    @NotEmpty(message = "플래너 제목을 입력해주세요.")
    private String plannerName;

    @NotNull(message = "여행 시작일자를 선택해주세요.")
    private LocalDate startDate;

    @NotNull(message = "여행 종료일자를 선택해주세요.")
    private LocalDate endDate;

    private List<AddPlanRequest> plans; // Plan 정보를 담을 리스트

}