package com.io.threegonew.service;

import com.io.threegonew.domain.*;
import com.io.threegonew.dto.AddPlannerRequest;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    private final PlannerShareRepository plannerShareRepository;

    // 특정 Planner를 다른 유저와 공유
    public void sharePlanner(Long plannerId, String ownerId, List<String> sharedWithUserIds) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner user not found"));
        Planner planner = plannerRepository.findById(plannerId).orElseThrow(() -> new RuntimeException("Planner not found"));

        for (String userId : sharedWithUserIds) {
            User sharedWithUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Shared with user not found"));

            PlannerShare plannerShare = new PlannerShare();
            plannerShare.setPlanner(planner);
            plannerShare.setUser(sharedWithUser);
            plannerShareRepository.save(plannerShare);
        }
    }

    // 사용자가 공유받은 Planner들을 조회
    public List<PlannerResponse> findSharedPlanners(String userId) {
        List<PlannerResponse> sharedPlannerResponseList =
                plannerShareRepository.findByUserId(userId).stream()
                .map(plannerShare -> modelMapper.map(plannerShare.getPlanner(), PlannerResponse.class))
                .collect(Collectors.toList());
        return sharedPlannerResponseList;
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
