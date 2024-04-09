package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.AddPlanRequest;
import com.io.threegonew.dto.CompletePlannerRequest;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TourItemRepository tourItemRepository;
    private final PlanRepository planRepository;

//    // 유저가 특정 Planner를 공유받았는지 확인
//    public boolean isSharedUser(Long plannerId, String userId) {
//        Planner planner = plannerRepository.findById(plannerId)
//                .orElseThrow(() -> new RuntimeException("Planner not found"));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return plannerShareRepository.existsByPlannerAndUser(planner, user);
//    }

    // 날짜 간의 차이를 계산하는 메서드 추가
    public long getDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }


    // 특정 유저가 특정 플래너를 작성했는지 확인하는 메서드
    public boolean isUserPlannerOwner(String userId, Long plannerId) {
        return plannerRepository.existsByUserIdAndPlannerId(userId, plannerId);
    }

    public Planner findPlanner(Long plannerId) {
        return plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너 정보를 찾을 수 없습니다."));
    }
    public List<Planner> findAll() {
        return plannerRepository.findAll();
    }

    public List<PlannerResponse> findMyPlannerList(String userId) {
        List<PlannerResponse> plannerResponseList =
                plannerRepository.findByUserId(userId).stream()
                        .map(planner -> modelMapper.map(planner, PlannerResponse.class))
                        .sorted(Comparator.comparing(PlannerResponse::getPlannerId).reversed()) // 최신 것부터 정렬
                        .collect(Collectors.toList());
        return plannerResponseList;
    }

    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public List<Sigungu> findSigunguByAreaCode(Integer areaCode) {
        return sigunguRepository.findByAreaCode(areaCode);
    }


    public Planner save(String plannerName, LocalDate startDate, LocalDate endDate, String userId) {
        if(plannerRepository.existsByUserIdAndPlannerNameAndStartDateAndEndDate(userId, plannerName, startDate, endDate)){
            throw new IllegalArgumentException("같은 정보의 플래너가 이미 등록되어있습니다.");
        }
        // 이미 등록된 플래너가 아니라면 등록함
        Planner planner = plannerRepository.save(Planner.builder()
                .userId(userId)
                .plannerName(plannerName)
                .startDate(startDate)
                .endDate(endDate)
                .build()
        );
        return planner; // 저장된 엔터티 반환
    }

    public void deletePlannerAndRelatedPlans(Long plannerId) {

        // Planner에 속한 모든 Team을 조회합니다.
        List<Team> teams = teamRepository.findByPlannerPlannerId(plannerId);

        // 조회된 모든 Team을 삭제합니다.
        for (Team team : teams) {
            teamRepository.delete(team);
        }

        // Planner에 속한 모든 Plan을 조회합니다.
        List<Plan> plans = planRepository.findByPlannerId(plannerId);

        // 조회된 모든 Plan을 삭제합니다.
        for (Plan plan : plans) {
            planRepository.delete(plan);
        }

        // 마지막으로 Planner를 삭제합니다.
        plannerRepository.deleteById(plannerId);
    }
}
