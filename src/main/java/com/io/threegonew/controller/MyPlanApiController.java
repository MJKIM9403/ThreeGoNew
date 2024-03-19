package com.io.threegonew.controller;

import com.io.threegonew.domain.Plan;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.PlanService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPlanApiController {
    private final TourItemService tourItemService;
    private final PlanService planService;

    @PostMapping("/showplan")
    public ResponseEntity<PlanResponse> showPlan(@RequestBody PlanRequest request) {
        // 요청에서 plannerId와 day를 가져와 사용합니다.
        Long plannerId = request.getPlannerId();
        Integer day = request.getDay();

        // plannerId와 day를 사용하여 해당하는 plan을 조회합니다.
        List<Plan> planList = planService.findByPlannerIdAndDay(plannerId, day);

        // 조회한 plan을 PlanResponse에 추가합니다.
        PlanResponse response = PlanResponse.builder()
                .success(true)
                .plans(planList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
