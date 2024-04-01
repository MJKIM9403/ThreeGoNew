package com.io.threegonew.service;

import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.FollowDTO;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.repository.FollowRepository;
import com.io.threegonew.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public int countFollower(String userId) {
        return followRepository.countFollower(userId);
    }

    public int countFollowing(String userId) {
        return followRepository.countFollowing(userId);
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

    public Optional<Follow> findFollowing(FollowDTO followDTO) {
        User toUser = userRepository.findById(followDTO.getToUser().getId()).orElseThrow(()->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        User fromUser = userRepository.findById(followDTO.getToUser().getId()).orElseThrow(()->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        return followRepository.findByToUserAndFromUser(toUser, fromUser);
    }


    // 팔로우중인지 확인하기
    public boolean isFollowing(User toUser, User fromUser) {
        return followRepository.existsByToUserAndFromUser(toUser, fromUser);
    }

    // 유저가 팔로우한 유저의 리스트를 뽑기
//    public List<FollowDTO> findFollowingsByFollower(User toUser) {
//        List<FollowDTO> followingList = followRepository.findByToUser(toUser).stream()
//                .map(this::followMapper)
//                .collect(Collectors.toList());
//        return followingList;
//    }

    // 유저를 팔로우한 유저의 리스트를 뽑기
//    public List<FollowDTO> findFollowersByFollowing(User fromUser) {
//        List<FollowDTO> followingList = followRepository.findByFromUser(fromUser).stream()
//                .map(this::followMapper)
//                .collect(Collectors.toList());
//        return followingList;
//    }

//    private FollowDTO followMapper(Follow follow) {
//        return FollowDTO.builder()
//                    .id(follow.getId())
//                    .toUser(userInfoMapper(follow.getToUser()))
//                    .fromUser(userInfoMapper(follow.getFromUser()))
//                    .build();
//    }

    // 로그인한 유저의 팔로우 상태 확인 + 마이페이지 유저가 팔로잉한 유저리스트를 뽑기
    public List<FollowDTO> findFollowingsByFollowerWithFollowState(User loggedInUser, User toUser) {
        List<FollowDTO> followingList = followRepository.findByToUser(toUser).stream()
                .map(follow -> followingMapper(follow, loggedInUser))
                .collect(Collectors.toList());
        return followingList;
    }

    // 로그인한 유저의 팔로우 상태 확인 + 마이페이지 유저를 팔로우한 유저리스트를 뽑기
    public List<FollowDTO> findFollowersByFollowingWithFollowState(User loggedInUser, User fromUser) {
        List<FollowDTO> followersList = followRepository.findByFromUser(fromUser).stream()
                .map(follow -> followerMapper(follow, loggedInUser))
                .collect(Collectors.toList());
        return followersList;
    }


    // 팔로우 상태를 함께 매핑하여 FollowDTO 객체 생성
    private FollowDTO followerMapper(Follow follow, User loggedInUser) {
        boolean isFollowing = isFollowing(loggedInUser, follow.getToUser());

        return FollowDTO.builder()
                .id(follow.getId())
                .toUser(userInfoMapper(follow.getToUser()))
                .fromUser(userInfoMapper(follow.getFromUser()))
                .followState(isFollowing ? 1 : 0) // 팔로우 상태 설정
                .followCount(followRepository.countFollower(follow.getFromUser().getId())) // 로그인한 유저와 같은지 설정
                .build();
    }

    private FollowDTO followingMapper(Follow follow, User loggedInUser) {
        boolean isFollowing = isFollowing(loggedInUser, follow.getFromUser());

        return FollowDTO.builder()
                .id(follow.getId())
                .toUser(userInfoMapper(follow.getToUser()))
                .fromUser(userInfoMapper(follow.getFromUser()))
                .followState(isFollowing ? 1 : 0) // 팔로우 상태 설정
                .followCount(followRepository.countFollowing(follow.getToUser().getId())) // 로그인한 유저와 같은지 설정
                .build();
    }


    private UserInfoResponse userInfoMapper(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getU_sfile())
                .about(user.getAbout())
                .build();
    }



}
