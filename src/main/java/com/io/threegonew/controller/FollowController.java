package com.io.threegonew.controller;

import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.FollowDTO;
import com.io.threegonew.service.FollowService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
public class FollowController {
    private final UserService userService;
    private final FollowService followService;


    // 팔로우하기
    @PostMapping("/api/follow/{friendName}")
    public ResponseEntity follow(Authentication auth, @PathVariable("friendName") String friendName) {
        // 로그인 된 유저
        User toUser = userService.findUser(auth.getName());
        // 가 팔로할 사람
        User fromUser = userService.findUser(friendName);
        followService.follow(toUser, fromUser);
        return ResponseEntity.ok().build();
    }

    // 언팔로우하기
    @DeleteMapping("/api/unfollow/{friendName}")
    public ResponseEntity<?> unfollow(Authentication auth, @PathVariable("friendName") String friendName) {
        // 로그인 된 유저
        User toUser = userService.findUser(auth.getName());
        // 언팔할 사람
        User fromUser = userService.findUser(friendName);
        followService.unfollow(toUser, fromUser);
        return ResponseEntity.ok().build();
    }

    // 팔로우중인지 확인하기
//    @GetMapping("/api/isfollowing/{friendName}")
//    public ResponseEntity<Boolean> isFollowing(Authentication auth, @PathVariable("friendName") String friendName) {
//        // 로그인 된 유저
//        User toUser = userService.findUser(auth.getName());
//        // 확인할 대상
//        User fromUser = userService.findUser(friendName);
//        boolean isFollowing = followService.isFollowing(toUser, fromUser);
//        return ResponseEntity.ok(isFollowing);
//    }

    // 유저의 팔로잉리스트를 뽑기
//    @GetMapping("/api/show/followingList/{userId}")
//    public ResponseEntity<List<FollowDTO>> getFollowings(@PathVariable("userId") String userId) {
//        // 로그인 된 유저
//        User loggedInUser = userService.findUser(userService.getCurrentUserId());
//        // 대상 유저
//        User user = userService.findUser(userId);
//        List<FollowDTO> followings = followService.findFollowingsByFollower(user);
//        return ResponseEntity.ok(followings);
//    }
//
//    // 유저의 팔로워리스트를 뽑기
//    @GetMapping("/api/show/followerList/{userId}")
//    public ResponseEntity<List<FollowDTO>> getFollowers(@PathVariable("userId") String userId) {
//        String loginUserId = userService.getCurrentUserId();
//        // 로그인 된 유저
//        User loggedInUser = userService.findUser(userService.getCurrentUserId());
//        // 대상 유저
//        User user = userService.findUser(userId);
//        List<FollowDTO> followers = followService.findFollowersByFollowing(user);
//        return ResponseEntity.ok(followers);
//    }
// 유저의 팔로잉리스트를 불러오고 팔로우 상태를 함께 반환
    @GetMapping("/api/show/followingList/{userId}")
    public ResponseEntity<List<FollowDTO>> getFollowingsWithFollowState(@PathVariable("userId") String userId) {
        // 로그인 된 유저
        User loggedInUser = userService.findUser(userService.getCurrentUserId());
        // 대상 유저
        User user = userService.findUser(userId);
        List<FollowDTO> followings = followService.findFollowingsByFollowerWithFollowState(user, loggedInUser);
        return ResponseEntity.ok(followings);
    }

    // 유저의 팔로워리스트를 불러오고 팔로우 상태를 함께 반환
    @GetMapping("/api/show/followerList/{userId}")
    public ResponseEntity<List<FollowDTO>> getFollowersWithFollowState(@PathVariable("userId") String userId) {
        // 로그인 된 유저
        User loggedInUser = userService.findUser(userService.getCurrentUserId());
        // 대상 유저
        User user = userService.findUser(userId);
        List<FollowDTO> followers = followService.findFollowersByFollowingWithFollowState(user, loggedInUser);
        return ResponseEntity.ok(followers);
    }



}
