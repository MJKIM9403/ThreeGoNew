package com.io.threegonew.service;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.dto.AddPlannerRequest;
import com.io.threegonew.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;

    public List<Planner> findAll() {
        return plannerRepository.findAll();
    }

    public Planner save(AddPlannerRequest dto) {
        return plannerRepository.save(Planner.builder()
                .plannerName(dto.getPlannerName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build()
        );
    }
}
