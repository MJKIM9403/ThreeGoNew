package com.io.threegonew.controller;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.TeamRepository;
import com.io.threegonew.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final PlanService planService;
    private final TourItemService tourItemService;
    private final TeamService teamService;
    private final PlannerService plannerService;


    @GetMapping("/city")
    public String getSelectList(HttpServletRequest request, Model model){
        String plannerName = request.getParameter("plannerName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        // String을 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

        // 날짜 차이 계산 (+1을 해서 시작일과 종료일 포함)
        long daysBetween = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;

        System.out.println("플래너 이름 : " + plannerName);
        System.out.println("시작일 : " + startDate);
        System.out.println("시작일 : " + startLocalDate);
        System.out.println("종료일 : " + endDate);
        System.out.println("종료일 : " + endLocalDate);
        System.out.println("날짜 차이 : " + daysBetween);

        // 1부터 daysBetween까지의 숫자 목록 생성
        List<Integer> dayNumbers = new ArrayList<>();
        for (int i = 1; i <= daysBetween; i++) {
            dayNumbers.add(i);
        }

        System.out.println("날짜 차이: " + daysBetween);
        model.addAttribute("plannerName", plannerName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("daysBetween", daysBetween);
        model.addAttribute("dayNumbers", dayNumbers);
        // 여기까지 날짜부분


        TourItemSelectRequest tourItemSelectRequest = buildTourItemSelectRequest();
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(tourItemSelectRequest);


        /* 선택한 지역의 내 북마크 목록 조회 테스트용 코드*/
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = "";

        if (principal.equals("anonymousUser")) {
            userId = "anonymousUser";
        }else {
            UserDetails userDetails = (UserDetails)principal;
            userId = userDetails.getUsername();
        }
        PageResponse myPageResponse = tourItemService.findMyBookmark(MyPageRequest.builder()
                        .userId(userId)
                        .build());

        model.addAttribute("myPageResponse", myPageResponse);
        model.addAttribute("userId", userId);
        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", new ArrayList<Sigungu>());
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        model.addAttribute("selectedArea", "전체");
        model.addAttribute("pageResponse", pageResponse);



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
        String userId = "";

        if (principal.equals("anonymousUser")) {
            userId = "anonymousUser";
        }else {
            UserDetails userDetails = (UserDetails)principal;
            userId = userDetails.getUsername();
        }

        System.out.println("plannerId : " + plannerId); // 플래너 아이디 확인
        String plannerName = plannerService.findPlanner(plannerId).getPlannerName(); // 플래너 이름
        List<Plan> plans = planService.findByPlannerId(plannerId); // 플랜

        // plannerId에 해당하는 planner의 날짜 계산하기
        Planner optionalPlanner = plannerService.findPlanner(plannerId);
        LocalDate startLocalDate = optionalPlanner.getStartDate();
        LocalDate endLocalDate = optionalPlanner.getEndDate();
        String startDate = startLocalDate.toString();
        String endDate = endLocalDate.toString();
        long daysBetween = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;

        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(daysBetween);

        // 1부터 daysBetween까지의 숫자 목록 생성
        List<Integer> dayNumbers = new ArrayList<>();
        for (int i = 1; i <= daysBetween; i++) {
            dayNumbers.add(i);
        }

        // plannerId가 일치하는 planList 찾기
        List<TourItem> planList = new ArrayList<>();

        for (Plan plan : plans) {
            planList.add(plan.getTourItem());
        }

        model.addAttribute("userId", userId);
        model.addAttribute("plans", plans);
        model.addAttribute("plannerId", plannerId);
        model.addAttribute("plannerName", plannerName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("daysBetween", daysBetween);
        model.addAttribute("dayNumbers", dayNumbers);

        TourItemSelectRequest tourItemSelectRequest = buildTourItemSelectRequest();
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(tourItemSelectRequest);


        /* 선택한 지역의 내 북마크 목록 조회 테스트용 코드*/

        PageResponse myPageResponse = tourItemService.findMyBookmark(MyPageRequest.builder()
                .userId(userId)
                .build());

        model.addAttribute("myPageResponse", myPageResponse);
        model.addAttribute("userId", userId);
        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        model.addAttribute("sigunguList", new ArrayList<Sigungu>());
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        model.addAttribute("selectedArea", "전체");
        model.addAttribute("pageResponse", pageResponse);

        return "plan/edit";

    }

    @GetMapping("/show")
    public String ShowYourPlan (
                                    @RequestParam(name = "p_id") Long plannerId,
                                    Model model
                                ) {

        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        System.out.println("plannerId : " + plannerId); // 플래너 아이디 확인
        String plannerName = plannerService.findPlanner(plannerId).getPlannerName(); // 플래너 이름
        List<Plan> plans = planService.findByPlannerId(plannerId); // 플랜



        // plannerId에 해당하는 planner의 날짜 계산하기
        Planner optionalPlanner = plannerService.findPlanner(plannerId);
        LocalDate startLocalDate = optionalPlanner.getStartDate();
        LocalDate endLocalDate = optionalPlanner.getEndDate();
        long daysBetween = plannerService.getDaysBetweenDates(startLocalDate, endLocalDate);
        String startDateString = startLocalDate.toString(); // LocalDate를 문자열로 변환
        String endDateString = endLocalDate.toString(); // LocalDate를 문자열로 변환

        // plannerId가 일치하는 planList 찾기
        List<TourItem> planList = new ArrayList<>();

        for (Plan plan : plans) {
            planList.add(plan.getTourItem());
        }

        // 사용자의 아이디와 동일한 Planner가 있는지 확인
        List<PlannerResponse> plannerList = plannerService.findPlannerByUser(userId);

        // 사용자가 해당 플래너를 작성한 사람인지 확인
        boolean isWriter = plannerService.isUserPlannerOwner(userId, plannerId);

        // 사용자가 해당 플래너를 공유받았는지 확인
        int isGuest = plannerService.isUserPlannerGuest(plannerId, userId);


        // 내가 작성한 plannerList
        if(!userId.equals("anonymousUser")) {
            model.addAttribute("plannerList", plannerList);
        } else {
            model.addAttribute("plannerList", new ArrayList<Planner>());
        }

        // 게스트 리스트 조회
        List<TeamUserResponse> guestList = teamService.getGuestsOfPlanner(plannerId);

        // 전체 팀 리스트 조회
        List<TeamUserResponse> teamList = teamService.getAllMembersOfPlanner(plannerId);


        // 모델에 groupedPlans와 Plan과 TourItem 정보를 넣어서 view로 전달
        model.addAttribute("userId", userId);
        model.addAttribute("plans", plans);
        model.addAttribute("plannerId", plannerId);
        model.addAttribute("plannerName", plannerName);
        model.addAttribute("startLocalDate", startLocalDate);
        model.addAttribute("endLocalDate", endLocalDate);
        model.addAttribute("startDateString", startDateString);
        model.addAttribute("endDateString", endDateString);
        model.addAttribute("daysBetween", daysBetween);
        model.addAttribute("guestList", guestList);
        model.addAttribute("teamList", teamList);
        model.addAttribute("isWriter", isWriter);
        model.addAttribute("isGuest", isGuest);

        return "plan/showplan";
    }
}

