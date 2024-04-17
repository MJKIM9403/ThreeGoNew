package com.io.threegonew.repository;

import com.io.threegonew.domain.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    boolean existsByUserIdAndPlannerNameAndStartDateAndEndDate(String userId, String plannerName, LocalDate startDate, LocalDate endDate);
    boolean existsByUserIdAndPlannerId(String userId, Long plannerId);
    Optional<Planner> findByPlannerId(Long plannerId);

    @Query(value = "select distinct p.* " +
            "from planner p " +
            "left join team t " +
            "on p.p_id = t.p_id " +
            "where (p.u_id = :userId or t.u_id = :userId) " +
            "and p.p_del = 0 " +
            "order by p_id desc ",
           nativeQuery = true)
    List<Planner> findByMyPlanners(@Param("userId") String userId);

    List<Planner> findByUserId(String userId);
    // 유무 확인 나중에 추가
}
