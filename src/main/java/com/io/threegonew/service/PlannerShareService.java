package com.io.threegonew.service;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.PlannerShare;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.repository.PlannerRepository;
import com.io.threegonew.repository.PlannerShareRepository;
import com.io.threegonew.repository.TourItemRepository;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerShareService {
    private final PlannerShareRepository plannerShareRepository;
    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final TourItemRepository tourItemRepository;

    @Transactional
    public void sharePlannerWithUser(Long plannerId, String userId) {
        Planner planner = plannerRepository.findByPlannerId(plannerId)
                .orElseThrow(()-> new IllegalArgumentException("Planner not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        PlannerShare plannerShare = new PlannerShare(planner, user);
        plannerShareRepository.save(plannerShare);
    }

    @Transactional
    public void sharePlannerWithMultipleUsers(Long plannerId, List<String> userIds) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found"));

        userIds.forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            PlannerShare plannerShare = new PlannerShare(planner, user);
            plannerShareRepository.save(plannerShare);
        });
    }
}
