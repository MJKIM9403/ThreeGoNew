package com.io.threegonew.service;

import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.FollowDTO;
import com.io.threegonew.repository.FollowRepository;
import com.io.threegonew.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;


    @Transactional
    public FollowDTO checkFollowState(User toUser, User fromUser) {
        if (toUser.equals(fromUser)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        if (followRepository.existsByToUserAndFromUser(toUser, fromUser)) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }

        // 팔로우 상태 확인
        int followState = 1; // 팔로우 상태로 설정

        // Follow 엔티티 생성 및 저장
        Follow follow = Follow.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .build();
        followRepository.save(follow);

        // FollowDTO 생성 및 반환
        return FollowDTO.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .followState(followState)
                .build();
    }

    // 팔로우하기
    @Transactional
    public void follow(User toUser, User fromUser) {
        if (toUser.equals(fromUser)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        if (followRepository.existsByToUserAndFromUser(toUser, fromUser)) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }
        Follow follow = Follow.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .build();
        followRepository.save(follow);
    }

    // 언팔로우하기
    @Transactional
    public void unfollow(User toUser, User fromUser) {
        Follow follow = followRepository.findByToUserAndFromUser(toUser, fromUser)
                .orElseThrow(() -> new IllegalArgumentException("팔로우하지 않은 사용자입니다."));
        followRepository.delete(follow);
    }

    // 팔로우중인지 확인하기
    public boolean isFollowing(User toUser, User fromUser) {
        return followRepository.existsByToUserAndFromUser(toUser, fromUser);
    }

    // 유저가 팔로우한 유저의 리스트를 뽑기
    public List<Follow> findFollowingsByFollower(User toUser) {
        return followRepository.findByToUser(toUser);
    }

    // 유저를 팔로우한 유저의 리스트를 뽑기
    public List<Follow> findFollowersByFollowing(User fromUser) {
        return followRepository.findByFromUser(fromUser);
    }

}
