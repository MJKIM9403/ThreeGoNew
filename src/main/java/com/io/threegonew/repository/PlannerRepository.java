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

    // 로그인한 유저가 플래너를 공유했는지 확인하기
    @Query(value = "SELECT EXISTS(SELECT 1 FROM team WHERE p_id = :plannerId" +
            " AND u_id = :loginId " +
            " AND team_level = 9) ", nativeQuery = true)
    int existsByPlannerIdAndLoginIdAndHost(@Param("plannerId") Long plannerId, @Param("loginId") String loginId);

    // 로그인한 유저가 플래너를 공유받았는지 확인하기
    @Query(value = "SELECT EXISTS(SELECT 1 FROM team WHERE p_id = :plannerId" +
            " AND u_id = :loginId " +
            " AND team_level = 0) ", nativeQuery = true)
    int existsByPlannerIdAndLoginIdAndGuest(@Param("plannerId") Long plannerId, @Param("loginId") String loginId);

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
