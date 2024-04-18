package com.io.threegonew.repository;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.Team;
import com.io.threegonew.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {


    List<Team> findByUserId(String userId);

    List<Team> findByPlannerPlannerId(Long PlannerId);

    Optional<Team> findByPlannerAndUserAndTeamLevel(Planner planner, User user, Integer teamLevel);

    // 접근 권한 확인하기
    Optional<Team> findByPlannerPlannerIdAndUserId(Long plannerId, String userId);


}
