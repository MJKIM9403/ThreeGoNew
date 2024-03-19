package com.io.threegonew.service;

import com.io.threegonew.domain.Area;
import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.Sigungu;
import com.io.threegonew.dto.AddPlannerRequest;
import com.io.threegonew.repository.AreaRepository;
import com.io.threegonew.repository.PlannerRepository;
import com.io.threegonew.repository.SigunguRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;

    public Planner findPlanner(Long plannerId) {
        return plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너 정보를 찾을 수 없습니다."));
    }
    public List<Planner> findAll() {
        return plannerRepository.findAll();
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
