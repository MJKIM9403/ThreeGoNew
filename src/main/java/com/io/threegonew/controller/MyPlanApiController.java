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
    public ResponseEntity<List<PlanDTO>> showPlan(@RequestBody PlanRequest request) {
        List<PlanDTO> planDtos = planService.findByPlannerIdAndDay(request);
        return new ResponseEntity<>(planDtos, HttpStatus.OK);
    }
}
