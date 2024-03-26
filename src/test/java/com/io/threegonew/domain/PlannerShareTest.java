//package com.io.threegonew.domain;
//
//import com.io.threegonew.service.PlannerService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class PlannerShareTest {
//    @Autowired
//    private PlannerService plannerService;
//
//    @Test
//    public void testShareAndFindSharedPlanner() {
//        // 가정: 데이터가 사전에 적절히 설정되어 있음
//        String ownerId = "testtest1";
//        List<String> sharedWithUserIds = Arrays.asList("test2", "test3");
//        Long plannerId = 24L;
//
//        // Planner 공유 실행
//        plannerService.sharePlanner(plannerId, ownerId, sharedWithUserIds);
//
//        // 각 사용자별로 공유받은 Planner 조회
//        for (String userId : sharedWithUserIds) {
//            List<Planner> sharedPlanners = plannerService.findSharedPlanners(userId);
//
//            // 검증: 공유받은 Planner가 실제로 조회되는지 확인
//            assertFalse(sharedPlanners.isEmpty());
//            assertTrue(sharedPlanners.stream().anyMatch(planner -> planner.getPlannerId().equals(plannerId)));
//        }
//    }
//
//}