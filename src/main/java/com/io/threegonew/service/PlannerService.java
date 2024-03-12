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
        return plannerRepository.save(Planner.builder()
                .userId(userId)
                .areaCode(dto.getAreaCode())
                .plannerName(dto.getPlannerName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build()
        );
    }
}
