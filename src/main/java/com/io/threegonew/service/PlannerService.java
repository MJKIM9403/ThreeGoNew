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
