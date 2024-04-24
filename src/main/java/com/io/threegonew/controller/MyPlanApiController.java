package com.io.threegonew.controller;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.PlanService;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.TeamService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPlanApiController {
    private final TourItemService tourItemService;
    private final PlanService planService;
    private final PlannerService plannerService;
    private final TeamService teamService;

    /** 타임리프 관련 메서드 **/
    // 내가 북마크한 관광지를 조회할 때
    @GetMapping("/planner/bookmark/touritems")
    public String getMyBookmarkTourItemList(@ModelAttribute TourItemBookmarkRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findMyBookmarkedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "plan/plan :: #touritems";
    }

    // 내가 북마크한 관광지를 cat2로 조회할 때
    @GetMapping("/planner/bookmark/cat2")
    public String getMyBookmarkCat2List(@ModelAttribute TourItemBookmarkRequest request, Model model) {

        if(request.getCat1() != null){
            model.addAttribute("cat2List", tourItemService.findCat2List(request.getCat1()));
            model.addAttribute("cat3List", new ArrayList<>());
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan :: #category-middle";
    }

    // 내가 북마크한 관광지를 시군구로 조회할 때
    @GetMapping("/planner/bookmark/sigungu")
    public String getMyBookmarkSigunguList(@ModelAttribute TourItemBookmarkRequest request, Model model) {

        if(request.getAreaCode() != null){
            model.addAttribute("sigunguList", tourItemService.findSigunguList(Integer.valueOf(request.getAreaCode())));

        }else {
            model.addAttribute("sigunguList", new ArrayList<>());
        }

        return "plan/plan :: #sigungu";
    }

    // 내가 북마크한 관광지를 cat3로 조회할 때
    @GetMapping("/planner/bookmark/cat3")
    public String getMyBookmarkCat3List(@ModelAttribute TourItemBookmarkRequest request, Model model){
        if(request.getCat2() != null){
            model.addAttribute("cat3List", tourItemService.findCat3List(request.getCat2()));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan :: #category-row";
    }


    // 북마크 아닌, 전체 관광지를 여러 옵션으로 검색하여 조회할 때
    @GetMapping("/planner/touritems")
    public String getTourItemList(@ModelAttribute TourItemSelectRequest request, Model model) {
        PageResponse pageResponse = tourItemService.findSelectedTourItemList(request);
        model.addAttribute("pageResponse", pageResponse);
        return "plan/plan :: #touritems";
    }

    // 북마크 아닌, 전체 관광지를 ca2로 조회할 때
    @GetMapping("/planner/cat2")
    public String getCat2List(@ModelAttribute TourItemSelectRequest request, Model model) {

        if(request.getCat1() != null){
            model.addAttribute("cat2List", tourItemService.findCat2List(request.getCat1()));
            model.addAttribute("cat3List", new ArrayList<>());
        }else {
            model.addAttribute("cat2List", new ArrayList<>());
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan :: #category-middle";
    }

    // 북마크 아닌, 전체 관광지를 시군구로 조회할 때
    @GetMapping("/planner/sigungu")
    public String getSigunguList(@ModelAttribute TourItemSelectRequest request, Model model) {

        if(request.getAreaCode() != null){
            model.addAttribute("sigunguList", tourItemService.findSigunguList(Integer.valueOf(request.getAreaCode())));
        }else {
            model.addAttribute("sigunguList", new ArrayList<>());
        }

        return "plan/plan :: #sigungu";
    }

    // 북마크 아닌, 전체 관광지를 cat3로 조회할 때
    @GetMapping("/planner/cat3")
    public String getCat3List(@ModelAttribute TourItemSelectRequest request, Model model){
        if(request.getCat2() != null){
            model.addAttribute("cat3List", tourItemService.findCat3List(request.getCat2()));
        }else {
            model.addAttribute("cat3List", new ArrayList<>());
        }

        return "plan/plan :: #category-row";
    }

    /** 생성 메서드 **/
    // 플래너 생성
    @PostMapping(value = "/planner", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @PutMapping(value = "/planner/{plannerId}/dates")
    public ResponseEntity<?> updatePlannerDates(@PathVariable("plannerId") Long plannerId, @RequestBody UpdatePlannerRequest request) {
        try {
            plannerService.updatePlannerDates(request.getPlannerId(), request.getStartDate(), request.getEndDate());
            return ResponseEntity.ok("날짜가 성공적으로 업데이트 되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("날짜 업데이트 실패: " + e.getMessage());
        }
    }

    @PutMapping(value = "/planner/{plannerId}/name")
    public ResponseEntity<?> updatePlannerName(@PathVariable Long plannerId, @RequestBody UpdatePlannerRequest request) {
        try {
            plannerService.updatePlannerName(plannerId, request.getPlannerName());
            return ResponseEntity.ok().body("플래너 제목이 성공적으로 업데이트 되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("플래너 제목 업데이트 실패 : " + e.getMessage());
        }
    }


    @PutMapping(value = "/planner/{plannerId}/plans", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    /** 삭제 메서드 **/
    // 플래너 삭제
    @DeleteMapping(value = "/planner/{plannerId}")
    public ResponseEntity<String> updatePlannerDeleteFlag(@PathVariable Long plannerId) {
        try {
            plannerService.updatePlannerDeleteFlag(plannerId);
            return ResponseEntity.ok().body("플래너와 관련된 삭제 플래그가 성공적으로 수정되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("플래너와 관련된 삭제 플래그를 수정하는 데 실패했습니다");
        }
    }


    /** 공유 관련 메서드 **/
    // 플랜 공유받았는지 확인하기
    @GetMapping("/checkInvitation/{plannerId}/{guestId}")
    public ResponseEntity<Map<String, Boolean>> checkInvitation(@PathVariable Long plannerId, @PathVariable String guestId) {
        boolean invited = teamService.isGuestAlreadyInvited(plannerId, guestId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("invited", invited);
        return ResponseEntity.ok().body(response);
    }

    // 플래너 공유하기
    @PostMapping("/sharePlanner")
    public ResponseEntity<?> sharePlanner(@RequestBody SharePlannerRequest request) {
        try {
            boolean isWriter = plannerService.isUserPlannerOwner(request.getOwnerId(), request.getPlannerId());
            if(isWriter == true) {
                teamService.sharePlanner(request.getPlannerId(), request.getOwnerId(), request.getSharedWithUserIds());
                return ResponseEntity.ok().body("플래너가 성공적으로 공유되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("플래너는 작성자만 공유할 수 있습니다.");
            }


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("플래너 공유 실패: " + e.getMessage());
        }
    }

    // 플래너에서 게스트 삭제하기
    @DeleteMapping("/removeGuest/{plannerId}/{guestId}")
    public ResponseEntity<?> removeGuestFromPlanner(@PathVariable Long plannerId, @PathVariable String guestId) {
        try {
            teamService.removeGuestFromPlanner(plannerId, guestId);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }



    // 플랜을 날짜별로 가져와서 보여주기
//    @PostMapping("/showplan")
//    public ResponseEntity<List<PlanDTO<TourItemResponse>>> showPlan(@RequestBody PlanRequest request) {
//        List<PlanDTO<TourItemResponse>> planDTOs = planService.findByPlannerIdAndDay(request);
//        return new ResponseEntity<>(planDTOs, HttpStatus.OK);
//    }

    // 플랜 전체를 가져와서 보여주기
    @GetMapping("/allShowplan")
    public ResponseEntity<List<PlanDTO<TourItemResponse>>> allShowPlan(@ModelAttribute PlanRequest request) {
        List<PlanDTO<TourItemResponse>> planDTOs = planService.findByPlannerId(request);
        return new ResponseEntity<>(planDTOs, HttpStatus.OK);
    }




}
