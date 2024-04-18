package com.io.threegonew.service;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.Team;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.PlannerResponse;
import com.io.threegonew.dto.TeamUserResponse;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.repository.PlannerRepository;
import com.io.threegonew.repository.TeamRepository;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    // 플래너에 접근권한 확인하기
    public boolean hasAccessToPlanner(String userId, Long plannerId) {
        return teamRepository.findByPlannerPlannerIdAndUserId(plannerId, userId).isPresent();
    }


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
            // Team hostTeam = new Team(planner, host, 9);
            Team hostTeam = teamRepository.save(Team.builder()
                    .planner(planner)
                    .user(host)
                    .teamLevel(9)
                    .teamDelete(false)
                    .build()
            );
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
                    Team guestTeam = teamRepository.save(Team.builder()
                            .planner(planner)
                            .user(guest)
                            .teamLevel(0)
                            .teamDelete(false)
                            .build()
                    );
                }
            }
        }
    }

    // 게스트의 입장에서 공유받은 플래너들을 조회
    public List<PlannerResponse> findSharedPlanners(String userId) {
        List<PlannerResponse> sharedPlannerResponseList =
                teamRepository.findByUserId(userId).stream()

                        // 게스트인 경우만 필터링
                        .filter(team -> team.getTeamLevel() == 0)
                        // p_del 값이 false인 플래너만 필터링
                        .filter(team -> Boolean.FALSE.equals(team.getPlanner().getPlannerDelete()))

                        .map(team -> modelMapper.map(team.getPlanner(), PlannerResponse.class))
                        .sorted(Comparator.comparing(PlannerResponse::getPlannerId).reversed()) // 최신 것부터 정렬
                        .collect(Collectors.toList());
        return sharedPlannerResponseList;
    }

    private TeamUserResponse teamMapper(Team team) {
        return TeamUserResponse.builder()
                .plannerId(team.getPlanner().getPlannerId())
                .teamId(team.getTeamId())
                .user(userInfoMapper(team.getUser()))
                .teamLevel(team.getTeamLevel())
                .build();
    }

    private UserInfoResponse userInfoMapper(User user) {
        String defaultImage = "../assets/img/profile.jpg"; // 기본 이미지 경로 설정

        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getU_sfile()!= null && !user.getU_sfile().isEmpty() ? "/api/image/profile/" + user.getU_sfile() : defaultImage)
                .about(user.getAbout())
                .build();
    }

    // 특정 플래너의 멤버들을 전부 조회합니다.
    public List<TeamUserResponse> getAllMembersOfPlanner(Long plannerId) {
        // 특정 플래너에 속한 모든 팀 멤버를 조회합니다.
        List<TeamUserResponse> teamList = teamRepository.findByPlannerPlannerId(plannerId).stream()
                .map(this::teamMapper)
                .collect(Collectors.toList());

        return teamList;
    }

    // 특정 플래너의 게스트들을 조회합니다.
    public List<TeamUserResponse> getGuestsOfPlanner(Long plannerId) {
        // 특정 플래너에 속한 모든 팀 멤버를 조회합니다.
        List<TeamUserResponse> guestList = teamRepository.findByPlannerPlannerId(plannerId).stream()
                .filter(team -> team.getTeamLevel() == 0)
                .map(this::teamMapper)
                .collect(Collectors.toList());

        return guestList;
    }

    // 특정 플래너의 호스트를 조회합니다.
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




    // 특정 게스트가 특정 플래너에 이미 초대되었는지 확인합니다.
    public boolean isGuestAlreadyInvited(Long plannerId, String guestId) {
        List<TeamUserResponse> guests = getGuestsOfPlanner(plannerId);
        return guests.stream().anyMatch(guest -> guest.getUser().getId().equals(guestId));
    }

    // 특정 게스트를 삭제합니다.
    public void removeGuestFromPlanner(Long plannerId, String guestId) {
        // 플래너 조회
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found"));

        // 게스트(유저) 조회
        User guest = userRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 특정 플래너와 특정 게스트가 존재하는지 조회
        Optional<Team> teamOptional = teamRepository.findByPlannerAndUserAndTeamLevel(planner, guest, 0);

        if (teamOptional.isPresent()) {
            // 존재하면 삭제
            teamRepository.delete(teamOptional.get());
        } else {
            throw new RuntimeException("Guest not part of the planner");
        }
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
