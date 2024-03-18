package com.io.threegonew.repository;

import com.io.threegonew.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByPlannerId(Long plannerId);
    // 유무 확인 나중에 추가
}
