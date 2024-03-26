package com.io.threegonew.service;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.Team;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.repository.PlannerRepository;
import com.io.threegonew.repository.TeamRepository;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 특정 Planner를 다른 유저와 공유
    public void sharePlanner(Long plannerId, String hostId, List<String> guests) {
        User host = userRepository.findById(hostId).orElseThrow(() -> new RuntimeException("Owner user not found"));
        Planner planner = plannerRepository.findById(plannerId).orElseThrow(() -> new RuntimeException("Planner not found"));

        // Host가 이미 해당 Planner의 Host로 등록되어 있는지 확인
        Optional<Team> existingHostTeam = teamRepository.findByPlannerAndUserAndTeamLevel(planner, host, 9);

        if (existingHostTeam.isPresent()) {
            // 이미 Host로 등록된 경우, 기존 Team을 업데이트
            Team hostTeam = existingHostTeam.get();
            teamRepository.save(hostTeam);
        } else {
            // Host로 등록되어 있지 않은 경우, 새로운 Team 생성 및 저장
            Team hostTeam = new Team(planner, host, 9);
            teamRepository.save(hostTeam);
        }

        // host 이외의 사용자들을 게스트로 저장
        // 게스트를 저장
        for (String userId : guests) {
            if (!userId.equals(hostId)) {
                User guest = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Shared with user not found"));
                // 이미 해당 Planner의 Guest로 등록되어 있는지 확인
                Optional<Team> existingGuestTeam = teamRepository.findByPlannerAndUserAndTeamLevel(planner, guest, 0);

                if (!existingGuestTeam.isPresent()) {
                    // Guest로 등록되어 있지 않은 경우, 새로운 Team 생성 및 저장
                    Team guestTeam = new Team(planner, guest, 0);
                    teamRepository.save(guestTeam);
                }
            }
        }
    }

    // 게스트의 입장에서 공유받은 플래너들을 조회
    public List<PlannerResponse> findSharedPlanners(String userId) {
        List<PlannerResponse> sharedPlannerResponseList =
                teamRepository.findByUserId(userId).stream()

                        // 게스트인 경우만 필터링
                        .filter(team -> team.getTeamLevel() == 0) // 게스트인 경우만 필터링
                        .map(team -> modelMapper.map(team.getPlanner(), PlannerResponse.class))
                        .collect(Collectors.toList());
        return sharedPlannerResponseList;
    }

    // 특정 플래너의 게스트들을 조회합니다.
    public List<User> getGuestsOfPlanner(Long plannerId) {
        // 특정 플래너에 속한 모든 팀 멤버를 조회합니다.
        List<Team> teamMembers = teamRepository.findByPlannerPlannerId(plannerId);

        // 호스트를 필터링 하여 호스트를 제외한 게스트들만 선택합니다.
        List<User> guests = teamMembers.stream()
                .filter(team -> team.getTeamLevel() == 0) // 게스트 필터링
                .map(Team::getUser) // 사용자로 매핑
                .collect(Collectors.toList());

        return guests;
    }

    public Optional<User> getHostOfPlanner(Long plannerId) {
        // 특정 플래너에 대한 모든 팀 멤버를 조회합니다.
        List<Team> teamMembers = teamRepository.findByPlannerPlannerId(plannerId);

        // 호스트만 선택합니다.
        Optional<User> host = teamMembers.stream()
                .filter(team -> team.getTeamLevel() == 9)
                .map(Team::getUser)
                .findFirst();

        return host;
    }

    public boolean isGuestAlreadyInvited(Long plannerId, String guestId) {
        List<User> guests = getGuestsOfPlanner(plannerId);
        return guests.stream().anyMatch(guest -> guest.getId().equals(guestId));
    }


// 이전에 썼던거 밀고 다시 짭니다..
//    @Transactional
//    public void sharePlannerWithUser(Long plannerId, String userId) {
//        Planner planner = plannerRepository.findByPlannerId(plannerId)
//                .orElseThrow(()-> new IllegalArgumentException("Planner not found"));
//        User user = userRepository.findById(userId)
//                .orElseThrow(()-> new IllegalArgumentException("User not found"));
//
//        Team team = new Team(planner, user);
//        plannerShareRepository.save(team);
//    }
//
//    @Transactional
//    public void sharePlannerWithMultipleUsers(Long plannerId, List<String> userIds) {
//        Planner planner = plannerRepository.findById(plannerId)
//                .orElseThrow(() -> new IllegalArgumentException("Planner not found"));
//
//        userIds.forEach(userId -> {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
//            Team team = new Team(planner, user);
//            plannerShareRepository.save(team);
//        });
//    }
}
