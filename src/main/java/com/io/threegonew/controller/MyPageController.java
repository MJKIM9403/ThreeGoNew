package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.TourItemService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/{userId}")
public class MyPageController {
    private final UserService userService;

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
        return "mypage/mypage";
    }
}
