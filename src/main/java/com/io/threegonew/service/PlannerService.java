package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.MyPlannerResponse;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.repository.*;
import com.io.threegonew.util.AesUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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


    // 날짜 간의 차이를 계산하는 메서드 추가
    public long getDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
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

    public PlannerResponse findPlannerResponse(Long plannerId) {
        return plannerMapper(findPlanner(plannerId));
    }

    public List<PlannerResponse> findPlannerByUser(String userId) {
        List<PlannerResponse> plannerResponseList =
                plannerRepository.findByUserId(userId).stream()
                        .filter(planner -> Boolean.FALSE.equals(planner.getPlannerDelete())) // p_del 값이 false인 경우만
                        .map(this::plannerMapper)
                        .sorted(Comparator.comparing(PlannerResponse::getPlannerId).reversed()) // 최신 것부터 정렬
                        .collect(Collectors.toList());
        return plannerResponseList;
    }

    private PlannerResponse plannerMapper(Planner planner) {
        return PlannerResponse.builder()
                .plannerId(planner.getPlannerId())
                .userId(planner.getUserId())
                .plannerName(planner.getPlannerName())
                .startDate(planner.getStartDate())
                .endDate(planner.getEndDate())
                .plannerDelete(planner.getPlannerDelete())
                .isAfter(planner.getEndDate().compareTo(LocalDate.now()))
                .build();
    }


    public List<PlannerResponse> getCreatedOrSharedPlanners(String userId) {
        List<PlannerResponse> plannerResponseList =
                plannerRepository.findByMyPlanners(userId).stream()
                        .map(this::plannerMapper)
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
                .plannerDelete(false)
                .build()
        );
        return planner; // 저장된 엔터티 반환
    }

    @Transactional
    public void updatePlannerDeleteFlag(Long plannerId) {
        // Planner를 조회합니다.
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();

            // Planner의 삭제 플래그(p_del)를 true로 설정하고 저장합니다.
            planner.updateDelete();

            // Planner에 속한 모든 Plan을 조회하고 삭제 플래그를 true로 설정합니다.
            List<Plan> plans = planRepository.findByPlannerId(plannerId);
            for (Plan plan : plans) {
                plan.updateDelete();
            }

            // Planner에 속한 모든 Team을 조회하고 삭제 플래그를 true로 설정합니다.
            List<Team> teams = teamRepository.findByPlannerPlannerId(plannerId);
            for (Team team : teams) {
                team.updateDelete();
            }
        }
    }

    @Transactional
    public void updatePlannerName(Long plannerId, String plannerName) {
        // Planner를 조회합니다.
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();
            planner.updatePlannerName(plannerName);
        }
    }

    @Transactional
    public void updatePlannerDates(Long plannerId, LocalDate startDate, LocalDate endDate) {
        // Planner를 조회합니다.
        Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
        if (optionalPlanner.isPresent()) {
            Planner planner = optionalPlanner.get();
            LocalDate origStartDate = planner.getStartDate();
            LocalDate origEndDate = planner.getEndDate();
            // 원래 저장했던 일수
            long origDaysBetween = ChronoUnit.DAYS.between(origStartDate, origEndDate) + 1;
            // 수정할 일수
            long editDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

            if(editDaysBetween < origDaysBetween) {
                // 특정 p_id에 해당하는 Plan 중에서 editDaysBetween보다 큰 plan_day를 가진 Plan을 조회
                List<Plan> plansToDelete = planRepository.findByPlannerIdAndDayGreaterThan(plannerId, (int) editDaysBetween);
                // 조회된 Plan 삭제
                planRepository.deleteAll(plansToDelete);
            }

            planner.updatePlannerDays(startDate, endDate);
        }
    }
}
