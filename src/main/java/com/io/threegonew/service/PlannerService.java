package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.AddPlannerRequest;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
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

    public Planner save(AddPlannerRequest dto, String userId) {
        Planner planner = plannerRepository.save(Planner.builder()
                .userId(userId)
                .plannerName(dto.getPlannerName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build()
        );

        return planner; // 저장된 엔터티 반환
    }
}
