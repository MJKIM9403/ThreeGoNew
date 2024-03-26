package com.io.threegonew.controller;

import com.io.threegonew.domain.Area;
import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.Sigungu;
import com.io.threegonew.dto.AddPlanRequest;
import com.io.threegonew.dto.AddPlannerRequest;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/planner")
@RequiredArgsConstructor
public class PlannerController {
    private final HttpSession httpSession;
    private final PlannerService plannerService;
    private final TeamService teamService;


    @GetMapping
    public String showAreaCode(Model model) {


        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보에서 사용자 아이디를 추출
        String userId = authentication.getName();

        System.out.println(userId);

        // 사용자의 아이디와 동일한 Planner가 있는지 확인
        List<PlannerResponse> plannerList = plannerService.findMyPlannerList(userId);

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

        return "plan/calendar";
    }

    @PostMapping("/add")
    public String addPlanner(@ModelAttribute AddPlannerRequest request,
                             BindingResult result,
                             RedirectAttributes rttr) {

        if (result.hasErrors()) {
            // 바인딩 오류 처리
            System.out.println("error");; // 오류 페이지로 리다이렉트 또는 오류 메시지를 출력하는 뷰를 반환
        }

        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보에서 사용자 아이디를 추출
        String userId = authentication.getName();

        System.out.println(userId);
        System.out.println(request.getPlannerName());
        System.out.println(request.getStartDate());
        System.out.println(request.getEndDate());

        // Planner 저장 후 반환된 plannerId
        Long plannerId = plannerService.save(request, userId).getPlannerId();
        httpSession.setAttribute("plannerId", plannerId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(request.getStartDate());
        String endDate = dateFormat.format(request.getEndDate());

        // RedirectAttributes 를 사용해 URL에 파라미터 추가
        rttr.addAttribute("plannerName",request.getPlannerName());
        rttr.addAttribute("startDate",startDate);
        rttr.addAttribute("endDate",endDate);
        // 데이터 저장 후 원하는 페이지로 리다이렉트하거나 뷰 반환
        return "redirect:/plan/city";
    }
}