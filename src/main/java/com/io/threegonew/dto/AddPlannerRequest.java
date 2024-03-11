package com.io.threegonew.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddPlannerRequest {
    @NotEmpty(message = "플래너 제목을 입력해주세요.")
    private String plannerName;

    @NotNull(message = "여행 시작일자를 선택해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "여행 종료일자를 선택해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
