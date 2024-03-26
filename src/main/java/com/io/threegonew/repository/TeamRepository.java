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

    // 일단은 host를 반환하는데 씁니다
    Optional<Team> findByPlannerAndUserAndTeamLevel(Planner planner, User user, Integer teamLevel);

}
