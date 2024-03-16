package com.io.threegonew.controller;

import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.domain.Plan;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.PlanService;
import com.io.threegonew.service.TourItemContentService;
import com.io.threegonew.service.TourItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final TourItemService tourItemService;
    private final TourItemContentService tourItemContentService;
    private final HttpSession httpSession;




    @PostMapping("/api/touritems")
    public String getTourItemList(@RequestBody TourItemSelectRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "plan/plan2 :: #touritems";
    }

    @GetMapping("/city")
    public String getSelectList(@RequestParam(name = "plannerName") String plannerName,
                                @RequestParam(name = "startDate") String startDate,
                                @RequestParam(name = "endDate") String endDate,

//                                @PathVariable(name = "areaCode") Integer areaCode,
//                                @PathVariable(name = "sigunguCode", required = false) Integer sigunguCode,
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
        System.out.println("test3");
        model.addAttribute("areaList", tourItemService.findAreaList());
        model.addAttribute("cat1List", tourItemService.findCat1List());
        model.addAttribute("cat2List", new ArrayList<Cat2>());
        model.addAttribute("cat3List", new ArrayList<Cat3>());
        // model.addAttribute("sigunguList", tourItemService.findSigunguList(areaCode));
        model.addAttribute("contentTypeList", tourItemService.findContentTypeList());
        // model.addAttribute("selectedArea", tourItemService.findArea(areaCode));
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
    public String savePlan(@RequestBody List<AddPlanRequest> places,
                            RedirectAttributes rttr) {
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
                // 실패 응답 반환 또는 예외 처리
                System.out.println("plannerId 반환 실패~~~");
                return "error";
            }


            for(AddPlanRequest place: places) {
                place.setUserId(userId);
                place.setPlannerId(plannerId);
                planService.save(place, userId);
            }


            // RedirectAttributes 를 사용해 URL에 파라미터 추가
            rttr.addAttribute("p_id",plannerId);
            // 성공적으로 저장된 후의 로직 처리
//            return ResponseEntity.ok().body("Data saved successfully");
            return "redirect:/plan/show";


        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/show")
    public String ShowYourPlan (
                                    @RequestParam(name = "p_id") Long plannerId,
                                    Model model
                                ) {

        System.out.println("plannerId : " + plannerId);
        List<Plan> plans = planService.findByPlannerId(plannerId);
        List<TourItem> tourItems = new ArrayList<>();
        for (Plan plan : plans) {
            tourItems.add(plan.getTourItem());
        }

        // 모델에 Plan과 TourItem 정보를 넣어서 view로 전달
        model.addAttribute("plans", plans);
        model.addAttribute("tourItems", tourItems);


        return "plan/showplan";
    }
}

