package com.io.threegonew.repository;

import com.io.threegonew.domain.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    boolean existsByUserIdAndPlannerId(String userId, Long plannerId);
    Optional<Planner> findByPlannerId(Long plannerId);

    List<Planner> findByUserId(String userId);
    // 유무 확인 나중에 추가
}
