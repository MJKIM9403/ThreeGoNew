package com.io.threegonew.repository;

import com.io.threegonew.domain.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    Planner findByPlannerId(Long plannerId);
    // 유무 확인 나중에 추가
}
