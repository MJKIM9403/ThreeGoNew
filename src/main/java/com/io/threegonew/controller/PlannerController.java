package com.io.threegonew.controller;

import com.io.threegonew.dto.AddPlannerRequest;
import com.io.threegonew.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerService plannerService;

    @Autowired
    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/add")
    public String addPlanner(@ModelAttribute AddPlannerRequest request, BindingResult result) {
        if (result.hasErrors()) {
            // 바인딩 오류 처리
            System.out.println("error");; // 오류 페이지로 리다이렉트 또는 오류 메시지를 출력하는 뷰를 반환
        }

        System.out.println(request.getPlannerName());
        System.out.println(request.getStartDate());
        System.out.println(request.getEndDate());

        plannerService.save(request);

        // 데이터 저장 후 원하는 페이지로 리다이렉트하거나 뷰 반환
        return "redirect:/index";
    }
}