package com.io.threegonew.service;

import com.io.threegonew.domain.Area;
import com.io.threegonew.domain.Plan;
import com.io.threegonew.domain.Sigungu;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.AddPlanRequest;
import com.io.threegonew.repository.AreaRepository;
import com.io.threegonew.repository.PlanRepository;
import com.io.threegonew.repository.SigunguRepository;
import com.io.threegonew.repository.TourItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;
    private final TourItemRepository tourItemRepository;

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public List<Plan> findByPlannerId(Long plannerId) {
        return planRepository.findByPlannerId(plannerId);
    }

    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public List<Sigungu> findSigunguByAreaCode(Integer areaCode) {
        return sigunguRepository.findByAreaCode(areaCode);
    }

    public Plan save(AddPlanRequest dto, String userId) {
        // tourItem을 찾아서 설정
        TourItem tourItem = tourItemRepository.findById(dto.getContentId()).orElse(null);

        Plan plan = planRepository.save(Plan.builder()
                .userId(userId)
                .plannerId(dto.getPlannerId())
                .day(dto.getDay())
                .order(dto.getOrder())
                .tourItem(tourItem)
                .build());


        return plan;
    }
}
