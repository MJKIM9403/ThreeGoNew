package com.io.threegonew.repository;

import com.io.threegonew.domain.PlannerShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerShareRepository extends JpaRepository<PlannerShare, Long> {
    List<PlannerShare> findByUserId(String userId);
}
