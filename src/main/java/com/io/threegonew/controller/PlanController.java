package com.io.threegonew.controller;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("plan")
@RequiredArgsConstructor
public class PlanController {
    private final TourItemService tourItemService;
    private final TeamService teamService;
    private final PlannerService plannerService;


    @GetMapping("")
    public String getSelectList(HttpServletRequest request, Model model){
        String plannerName = request.getParameter("name");
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");

        // String을 LocalDate로 변환하고 날짜계산
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

        // 날짜 차이 계산 (+1을 해서 시작일과 종료일 포함)
        int daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1);

        // 현재 사용자의 인증 정보를 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginId = "";

        if (principal.equals("anonymousUser")) {
            loginId = "anonymousUser";
        }else {
            UserDetails userDetails = (UserDetails)principal;
            loginId = userDetails.getUsername();
        }

        PlannerResponse planner = PlannerResponse.builder()
                .userId(loginId)
                .plannerName(plannerName)
                .startDate(startLocalDate)
                .endDate(endLocalDate)
                .daysBetween(daysBetween)
                .build();

        TourItemSelectRequest tourItemSelectRequest = buildTourItemSelectRequest();
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(tourItemSelectRequest);

        /* 선택한 지역의 내 북마크 목록 조회용 코드*/
        PageResponse myPageResponse = tourItemService.findMyBookmark(MyPageRequest.builder()
                        .userId(loginId)
                        .build());

        model.addAttribute("loginId", loginId);
        model.addAttribute("planner", planner);

        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("myPageResponse", myPageResponse);

        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", new ArrayList<Sigungu>());
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());


        return "plan/plan";
    }

    private TourItemSelectRequest buildTourItemSelectRequest() {

        return TourItemSelectRequest.builder()
                .build();
    }


    @GetMapping("/edit")
    public String EditYourPlan ( @RequestParam(name = "p_id") Long plannerId, Model model) {
        // 현재 사용자의 인증 정보를 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginId = "";

        if (principal.equals("anonymousUser")) {
            loginId = "anonymousUser";
        }else {
            UserDetails userDetails = (UserDetails)principal;
            loginId = userDetails.getUsername();
        }

        // 플래너 조회하기
        PlannerResponse planner = plannerService.findPlannerResponse(plannerId);

        TourItemSelectRequest tourItemSelectRequest = buildTourItemSelectRequest();
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(tourItemSelectRequest);


        /* 선택한 지역의 내 북마크 목록 조회용 코드*/
        PageResponse myPageResponse = tourItemService.findMyBookmark(MyPageRequest.builder()
                .userId(loginId)
                .build());


        model.addAttribute("loginId", loginId);
        model.addAttribute("planner", planner);

        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("myPageResponse", myPageResponse);

        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", new ArrayList<Sigungu>());
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());

        return "plan/edit";

    }

    @GetMapping("/show")
    public String ShowYourPlan (
                                    @RequestParam(name = "p_id") Long plannerId,
                                    Model model
                                ) {

        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        // 플래너 조회하기
        PlannerResponse planner = plannerService.findPlannerResponse(plannerId);

        // 사용자가 해당 플래너를 작성한 사람인지 확인
        boolean isWriter = plannerService.isUserPlannerOwner(loginId, plannerId);

        // 사용자가 해당 플래너를 공유받았는지 확인
        boolean isGuest = plannerService.isUserPlannerGuest(plannerId, loginId) == 1;

        // 전체 팀 리스트 조회
        List<TeamUserResponse> teamList = teamService.getAllMembersOfPlanner(plannerId);


        // 모델에 플래너와, 플랜, 공유중인 팀 리스트 정보를 넣어서 view로 전달
        model.addAttribute("loginId", loginId);
        model.addAttribute("planner", planner);

        model.addAttribute("teamList", teamList);
        model.addAttribute("isWriter", isWriter);
        model.addAttribute("isGuest", isGuest);

        return "plan/showplan";
    }
}

