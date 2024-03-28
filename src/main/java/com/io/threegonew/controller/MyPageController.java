package com.io.threegonew.controller;

import com.io.threegonew.domain.Follow;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.FollowService;
import com.io.threegonew.service.TourItemService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/{userId}")
public class MyPageController {
    private final UserService userService;
    private final FollowService followService;

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

        // 팔로우, 팔로워 기능 관련 추가
//        User user = userService.findUser(userId);
//        List<Follow> followingList = followService.findFollowingsByFollower(user);
//        List<Follow> followerList = followService.findFollowersByFollowing(user);
//        model.addAttribute("followingList", followingList);
//        model.addAttribute("followerList", followerList);


        return "mypage/mypage";
    }
}
