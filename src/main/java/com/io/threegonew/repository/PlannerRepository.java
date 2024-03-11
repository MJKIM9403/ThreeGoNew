package com.io.threegonew.repository;

import com.io.threegonew.domain.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner, Integer> {

    // 유무 확인 나중에 추가
}
