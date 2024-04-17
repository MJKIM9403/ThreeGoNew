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
    private final TourItemContentService tourItemContentService;
    private final HttpSession httpSession;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final PlannerService plannerService;

    // 북마크 포스트 매핑
    @PostMapping("/api/bookmark")
    public String getMyBookmark(@RequestBody MyPageRequest request, Model model){
        PageResponse pageResponse = tourItemService.findMyBookmark(request);
        model.addAttribute(pageResponse);

        return "plan/plan2 :: #touritems";
    }

    @PostMapping("/api/bookmark/touritems")
    public String getMyBookmarkTourItemList(@RequestBody TourItemBookmarkRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findMyBookmarkedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "plan/plan2 :: #touritems";
    }

    @PostMapping("/api/bookmark/cat2")
    public String getMyBookmarkCat2List(@RequestBody TourItemBookmarkRequest request, Model model) {

        System.out.println("Cat1 : " + request.getCat1());

        if(request.getCat1() != null){
            model.addAttribute("cat2List", tourItemService.findCat2List(request.getCat1()));
            model.addAttribute("cat3List", new ArrayList<>());
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan2 :: #category-middle";
    }

    @PostMapping("/api/bookmark/sigungu")
    public String getMyBookmarkSigunguList(@RequestBody TourItemBookmarkRequest request, Model model) {

        System.out.println("areaCode : " + request.getAreaCode());

        if(request.getAreaCode() != null){
            model.addAttribute("sigunguList", tourItemService.findSigunguList(Integer.valueOf(request.getAreaCode())));

        }else {
            model.addAttribute("sigunguList", new ArrayList<>());
        }

        return "plan/plan2 :: #sigungu";
    }

    @PostMapping("/api/bookmark/cat3")
    public String getMyBookmarkCat3List(@RequestBody TourItemBookmarkRequest request, Model model){
        if(request.getCat2() != null){
            model.addAttribute("cat3List", tourItemService.findCat3List(request.getCat2()));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan2 :: #category-row";
    }


    // 북마크 아닌 경우
    @PostMapping("/api/touritems")
    public String getTourItemList(@RequestBody TourItemSelectRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "plan/plan2 :: #touritems";
    }

    @PostMapping("/api/cat2")
    public String getCat2List(@RequestBody TourItemSelectRequest request, Model model) {

        System.out.println("Cat1 : " + request.getCat1());

        if(request.getCat1() != null){
            model.addAttribute("cat2List", tourItemService.findCat2List(request.getCat1()));
            model.addAttribute("cat3List", new ArrayList<>());
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan2 :: #category-middle";
    }

    @PostMapping("/api/sigungu")
    public String getSigunguList(@RequestBody TourItemSelectRequest request, Model model) {

        System.out.println("areaCode : " + request.getAreaCode());

        if(request.getAreaCode() != null){
            model.addAttribute("sigunguList", tourItemService.findSigunguList(Integer.valueOf(request.getAreaCode())));

        }else {
            model.addAttribute("sigunguList", new ArrayList<>());
        }

        return "plan/plan2 :: #sigungu";
    }

    @PostMapping("/api/cat3")
    public String getCat3List(@RequestBody TourItemSelectRequest request, Model model){
        if(request.getCat2() != null){
            model.addAttribute("cat3List", tourItemService.findCat3List(request.getCat2()));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan2 :: #category-row";
    }

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



        return "plan/plan2";
    }

    private TourItemSelectRequest buildTourItemSelectRequest() {

        return TourItemSelectRequest.builder()
                .build();
    }

    @PostMapping(value = "/api/saveplans", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> savePlan(@RequestBody CompletePlannerRequest request) {

        String plannerName = request.getPlannerName();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        List<AddPlanRequest> places = request.getPlans();

        try {
            // 여기서 날짜와 플래너 이름을 받아서 사용할 수 있습니다.
            System.out.println("Planner Name: " + plannerName);
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);

            // 현재 사용자의 인증 정보를 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 인증 정보에서 사용자 아이디를 추출
            String userId = authentication.getName();
            System.out.println(userId);

            // 플래너를 생성하고 식별자를 반환합니다.
            Planner planner = plannerService.save(plannerName, startDate, endDate, userId);
            Long plannerId = planner.getPlannerId();

            if (plannerId == null) {
                // plannerId가 null인 경우 처리
                System.out.println("plannerId 반환 실패~~~");
            } else {
                for(AddPlanRequest place: places) {
                    place.setUserId(userId);
                    place.setPlannerId(plannerId);
                    planService.save(place, userId);
                }
            }

            return ResponseEntity.ok().body(Collections.singletonMap("p_id", plannerId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("p_id", null));
        }
    }

    /** 수정 메서드 **/
    @PostMapping(value = "api/editplannerDates")
    public ResponseEntity<?> updatePlannerDates(@RequestBody UpdatePlannerDatesRequest request) {
        try {
            plannerService.updatePlannerDates(request.getPlannerId(), request.getStartDate(), request.getEndDate());
            return ResponseEntity.ok("날짜가 성공적으로 업데이트 되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("날짜 업데이트 실패: " + e.getMessage());
        }
    }

    @PostMapping(value = "/api/editplannerName/{plannerId}/{plannerName}")
    public ResponseEntity<?> updatePlannerName(@PathVariable Long plannerId, @PathVariable String plannerName) {
        try {
            plannerService.updatePlannerName(plannerId, plannerName);
            return ResponseEntity.ok().body("플래너 제목이 성공적으로 업데이트 되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("플래너 제목 업데이트 실패 : " + e.getMessage());
        }
    }


    @PostMapping(value = "/api/editplans/{plannerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> updatePlan(@PathVariable("plannerId") Long plannerId, @RequestBody CompletePlannerRequest request) {

        String plannerName = request.getPlannerName();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        List<AddPlanRequest> places = request.getPlans();

        try {
            // 여기서 날짜와 플래너 이름을 받아서 사용할 수 있습니다.
            System.out.println("Planner Name: " + plannerName);
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);

            // 현재 사용자의 인증 정보를 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 인증 정보에서 사용자 아이디를 추출
            String userId = authentication.getName();
            System.out.println(userId);

            // 기존 계획을 삭제
            planService.deleteByPlannerId(plannerId); // 이 메서드도 필요에 따라 구현되어야 함

            // 플래너를 업데이트하고 식별자를 반환
            // Planner planner = plannerService.update(plannerId, plannerName, startDate, endDate, userId);


                for(AddPlanRequest place: places) {
                    place.setUserId(userId);
                    place.setPlannerId(plannerId);
                    planService.save(place, userId); // 저장된 엔터티 반환
                }


            return ResponseEntity.ok().body(Collections.singletonMap("p_id", plannerId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("p_id", null));
        }
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
        boolean exists = plannerService.isUserPlannerOwner(userId, plannerId);

        // 내가 작성한 plannerList
        if(!userId.equals("anonymousUser")) {
            model.addAttribute("plannerList", plannerList);
        } else {
            model.addAttribute("plannerList", new ArrayList<Planner>());
        }




        // 게스트+호스트 조회
        List<User> memberList = teamService.getAllMembersOfPlanner(plannerId);

        // 게스트 리스트 조회
        List<User> guestList = teamService.getGuestsOfPlanner(plannerId);

        // 전체 팀 리스트 조회
        List<Team> teamList = teamRepository.findByPlannerPlannerId(plannerId);


        // 팀 리스트중에 로그인한 유저와 아이디가 같다면 팀 멤버 개인을 모델에 넣어 전달
        for(Team teamMember: teamList) {
            if(teamMember.getUser().getId().equals(userId)) {
                model.addAttribute("teamMember",teamMember);
            }
        }

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
        model.addAttribute("exists", exists);

        return "plan/showplan2";
    }

    @PostMapping(value = "/api/delete/{plannerId}")
    public ResponseEntity<String> updatePlannerDeleteFlag(@PathVariable Long plannerId) {
        try {
            plannerService.updatePlannerDeleteFlag(plannerId);
            return ResponseEntity.ok().body("플래너와 관련된 삭제 플래그가 성공적으로 수정되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("플래너와 관련된 삭제 플래그를 수정하는 데 실패했습니다");
        }
    }
}

