package com.io.threegonew.controller;

import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.domain.Cat2;
import com.io.threegonew.domain.Cat3;
import com.io.threegonew.dto.BookmarkRequest;
import com.io.threegonew.dto.MyBookmarkByAreaRequest;
import com.io.threegonew.dto.PageResponse;
import com.io.threegonew.dto.TourItemSelectRequest;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.TourItemContentService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlannerService plannerService;
    private final TourItemService tourItemService;
    private final TourItemContentService tourItemContentService;

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
        System.out.println("test1");

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
}

