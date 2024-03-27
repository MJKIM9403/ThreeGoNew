package com.io.threegonew.controller;

import com.io.threegonew.domain.Plan;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.PlanService;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.TeamService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPlanApiController {
    private final TourItemService tourItemService;
    private final PlanService planService;
    private final PlannerService plannerService;
    private final TeamService teamService;

    // 플랜 공유받았는지 확인하기
    @GetMapping("/checkInvitation/{plannerId}/{guestId}")
    public ResponseEntity<Map<String, Boolean>> checkInvitation(@PathVariable Long plannerId, @PathVariable String guestId) {
        boolean invited = teamService.isGuestAlreadyInvited(plannerId, guestId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("invited", invited);
        return ResponseEntity.ok().body(response);
    }

    // 플랜 가져와서 보여주기
    @PostMapping("/showplan")
    public ResponseEntity<List<PlanDTO>> showPlan(@RequestBody PlanRequest request) {
        List<PlanDTO> planDtos = planService.findByPlannerIdAndDay(request);
        return new ResponseEntity<>(planDtos, HttpStatus.OK);
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
}
