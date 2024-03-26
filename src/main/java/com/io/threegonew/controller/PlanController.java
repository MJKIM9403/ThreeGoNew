package com.io.threegonew.controller;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final TourItemService tourItemService;
    private final TourItemContentService tourItemContentService;
    private final HttpSession httpSession;
    private final TeamService teamService;

    @PostMapping("/api/search")
    public String searchTourItems(@RequestBody TourItemSelectRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "plan/plan2 :: #touritems";
    }

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
    public String getSelectList(@RequestParam(name = "plannerName") String plannerName,
                                @RequestParam(name = "startDate") String startDate,
                                @RequestParam(name = "endDate") String endDate,
                                Model model){

        // String을 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

        // 날짜 차이 계산 (+1을 해서 시작일과 종료일 포함)
        long daysBetween = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;

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


        TourItemSelectRequest request = buildTourItemSelectRequest();
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);


        /* 선택한 지역의 내 북마크 목록 조회 테스트용 코드*/
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String userId = "";
//
//        if (principal.equals("anonymousUser")) {
//            userId = "anonymousUser";
//        }else {
//            UserDetails userDetails = (UserDetails)principal;
//            userId = userDetails.getUsername();
//        }
//        PageResponse pageResponse = tourItemService.findMyBookmarkByArea(MyBookmarkByAreaRequest.builder()
//                        .areacode(String.valueOf(areaCode))
//                        .userId(userId)
//                        .build());



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


//    private TourItemSelectRequest buildTourItemSelectRequest(Integer areaCode, Integer sigunguCode) {
//        String AreaCode = (areaCode == null ) ? null : String.valueOf(areaCode);
//        String SigunguCode = (sigunguCode == null) ? null : String.valueOf(sigunguCode);
//
//        return TourItemSelectRequest.builder()
//                .areaCode(AreaCode)
//                .sigunguCode(SigunguCode)
//                .build();
//    }



    @PostMapping(value = "/api/saveplans", consumes = MediaType.APPLICATION_JSON_VALUE) // consumes 설정 추가
    public ResponseEntity<Map<String, Long>> savePlan(@RequestBody List<AddPlanRequest> places) {
        // places 데이터를 처리하고 데이터베이스에 저장하는 로직
        try {
            // 현재 사용자의 인증 정보를 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 인증 정보에서 사용자 아이디를 추출
            String userId = authentication.getName();

            System.out.println(userId);
            Long plannerId = (Long) httpSession.getAttribute("plannerId");

            if (plannerId == null) {
                // plannerId가 null인 경우 처리
                System.out.println("plannerId 반환 실패~~~");
            }


            for(AddPlanRequest place: places) {
                place.setUserId(userId);
                place.setPlannerId(plannerId);
                planService.save(place, userId);
            }

            // 성공적으로 저장된 후의 로직 처리
            // JSON 객체로 p_id를 포함하여 반환
            return ResponseEntity.ok().body(Collections.singletonMap("p_id", plannerId));


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("p_id", null));
        }
    }

    @GetMapping("/show")
    public String ShowYourPlan (
                                    @RequestParam(name = "p_id") Long plannerId,
                                    Model model
                                ) {

        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        System.out.println("plannerId : " + plannerId);
        List<Plan> plans = planService.findByPlannerId(plannerId);

        Optional<Plan> maxDayOptional = planService.findTopByPlannerIdOrderByDayDesc(plannerId);
        int maxDay = maxDayOptional.isPresent() ? maxDayOptional.get().getDay() : 0;

        List<TourItem> planList = new ArrayList<>();

        for (Plan plan : plans) {
            planList.add(plan.getTourItem());
        }

        List<User> guestList = teamService.getGuestsOfPlanner(plannerId);

        // 모델에 groupedPlans와 Plan과 TourItem 정보를 넣어서 view로 전달
        model.addAttribute("userId", userId);
        model.addAttribute("plans", plans);
        model.addAttribute("plannerId", plannerId);
        model.addAttribute("planList", planList);
        model.addAttribute("maxDay", maxDay);
        model.addAttribute("guestList", guestList);


        return "plan/showplan";
    }



}

