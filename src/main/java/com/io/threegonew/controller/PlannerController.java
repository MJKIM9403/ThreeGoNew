package com.io.threegonew.controller;

import com.io.threegonew.domain.Area;
import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.Sigungu;
import com.io.threegonew.dto.AddPlanRequest;
import com.io.threegonew.dto.AddPlannerRequest;
import com.io.threegonew.dto.MyPlannerResponse;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.TeamService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/planner")
@RequiredArgsConstructor
public class PlannerController {
    private final PlannerService plannerService;
    private final TeamService teamService;


    @GetMapping
    public String showAreaCode(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute(today);

        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보에서 사용자 아이디를 추출
        String userId = authentication.getName();

        System.out.println(userId);

        // 사용자의 아이디와 동일한 Planner가 있는지 확인
        List<PlannerResponse> plannerList = plannerService.findPlannerByUser(userId);

        // 내가 작성한 plannerList
        if(userId != null) {
            model.addAttribute("plannerList", plannerList);
        } else {
            model.addAttribute("plannerList", new ArrayList<Planner>());
        }

        // 내가 공유받은 Planner가 있는지 확인
        List<PlannerResponse> sharedPlannerList = teamService.findSharedPlanners(userId);

        if(userId != null) {
            model.addAttribute("sharedPlannerList", sharedPlannerList);
        } else {
            model.addAttribute("sharedPlannerList", sharedPlannerList);
        }

        return "plan/planner";
    }
}