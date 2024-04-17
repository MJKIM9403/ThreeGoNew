package com.io.threegonew.controller;

import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.FollowService;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.TourItemService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/{userId}")
public class MyPageController {
    private final UserService userService;
    private final FollowService followService;
    private final ReviewService reviewService;

    @GetMapping("")
    public String getMyPage(@PathVariable String userId, Model model) {
        boolean isUserExist = userService.isIdDuplicate(userId);

        if (!isUserExist) {
            return "mypage/notfounduser";
        }

        UserInfoResponse loginUserInfo;

        try{
            String loginUserId = userService.getCurrentUserId();
            loginUserInfo = userService.findUserInfo(loginUserId);
        }catch (IllegalArgumentException e){
            loginUserInfo = UserInfoResponse.builder()
                                        .id("anonymousUser")
                                        .build();
        }

        UserInfoResponse findUserInfo = userService.findUserInfo(userId);

        model.addAttribute("findUser", findUserInfo);
        model.addAttribute("loginUser", loginUserInfo);

        // 작성한 리뷰 개수
        Long reviewCount = reviewService.getMyReviewCount(userId);
        model.addAttribute("reviewCount", reviewCount);

//      팔로우, 팔로워 기능 관련 추가
        User fromUser = userService.findUser(userId);

        String myId = "";
        // 로그인 중인 유저
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal.equals("anonymousUser")) {
            myId = "anonymousUser";
        } else {
            UserDetails userDetails = (UserDetails) principal;
            myId = userDetails.getUsername();
            User toUser = userService.findUser(myId);

            boolean isFollowing = followService.isFollowing(toUser, fromUser);
            boolean isFollowed = followService.isFollowing(fromUser, toUser);
            model.addAttribute("isFollowing", isFollowing);
            model.addAttribute("isFollowed", isFollowed);
        }

        return "mypage/mypage";
    }
}
