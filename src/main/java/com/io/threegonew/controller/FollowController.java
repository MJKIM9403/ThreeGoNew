package com.io.threegonew.controller;

import com.io.threegonew.commons.SecurityUtils;
import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.ErrorResponse;
import com.io.threegonew.dto.FollowDTO;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.FollowService;
import com.io.threegonew.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequiredArgsConstructor
public class FollowController {
    private final UserService userService;
    private final FollowService followService;


    @GetMapping("/api/follow-count/{userId}")
    public ResponseEntity<?> getUserFollowCounts(@PathVariable String userId) {
        try {
            Map<String, Integer> followCounts;
            followCounts = new HashMap<>();
            int followerCount = followService.countFollower(userId);
            int followingCount = followService.countFollowing(userId);
            followCounts.put("followerCount", followerCount);
            followCounts.put("followingCount", followingCount);
            return ResponseEntity.ok(followCounts);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "팔로잉, 팔로우 수를 조회할 수 없습니다.");
        }
    }

    // 팔로우하기
    @PostMapping("/api/follow/{userId}")
    public ResponseEntity<?> follow(@PathVariable("userId") String userId) {
        try {
            String loginUserId = SecurityUtils.getCurrentUsername();
            if(loginUserId.equals("anonymousUser")) {
                return ErrorResponse.createErrorResponse(HttpStatus.UNAUTHORIZED, "401", "로그인하지 않은 사용자입니다.");
            } else {
                // 로그인 된 유저
                User toUser = userService.findUser(loginUserId);
                // 가 팔로할 사람
                User fromUser = userService.findUser(userId);

                followService.follow(toUser, fromUser);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "팔로우 할 수 없습니다.");
        }
    }

    // 언팔로우하기
    @DeleteMapping("/api/unfollow/{userId}")
    public ResponseEntity<?> unfollow(@PathVariable("userId") String userId) {
        try {
            String loginUserId = SecurityUtils.getCurrentUsername();
            if(loginUserId.equals("anonymousUser")) {
                return ErrorResponse.createErrorResponse(HttpStatus.UNAUTHORIZED, "401", "로그인하지 않은 사용자입니다.");
            } else {
                // 로그인 된 유저
                User toUser = userService.findUser(loginUserId);
                // 언팔할 사람
                User fromUser = userService.findUser(userId);
                followService.unfollow(toUser, fromUser);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "언팔로우 할 수 없습니다.");
        }
    }

    @GetMapping("/api/show/followinglist/{userId}")
    public ResponseEntity<?> getFollowingsWithFollowState(@PathVariable("userId") String userId) {

        try{
            String loginUserId = SecurityUtils.getCurrentUsername();
            if(loginUserId.equals("anonymousUser")) {
                return ErrorResponse.createErrorResponse(HttpStatus.UNAUTHORIZED, "401", "로그인하지 않은 사용자입니다.");
            } else {
                List<FollowDTO> followings = followService.showFollowingListWithFollowState(loginUserId, userId);
                return ResponseEntity.ok(followings);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "팔로잉리스트를 조회할 수 없습니다.");
        }
    }

    // 유저의 팔로워리스트를 불러오고 팔로우 상태를 함께 반환
    @GetMapping("/api/show/followerlist/{userId}")
    public ResponseEntity<?> getFollowersWithFollowState(@PathVariable("userId") String userId) {

        try{
            String loginUserId = SecurityUtils.getCurrentUsername();
            if(loginUserId.equals("anonymousUser")) {
                return ErrorResponse.createErrorResponse(HttpStatus.UNAUTHORIZED, "401", "로그인하지 않은 사용자입니다.");
            } else {
                List<FollowDTO> followers = followService.showFollowerListWithFollowState(loginUserId, userId);
                return ResponseEntity.ok(followers);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "팔로잉리스트를 조회할 수 없습니다.");
        }
    }
}
