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
    private final TourItemService tourItemService;
    private final UserService userService;

    @GetMapping("")
    public String getMyPage(@PathVariable String userId, Model model) {
        boolean isUserExist = userService.isIdDuplicate(userId);

        if (!isUserExist) {
            return "mypage/notfounduser";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginUserId = "";

        if (principal.equals("anonymousUser")) {
            loginUserId = "anonymousUser";
        } else {
            UserDetails userDetails = (UserDetails) principal;
            loginUserId = userDetails.getUsername();
        }
        UserInfoResponse findUserInfo = userService.findUserInfo(userId);
        model.addAttribute("findUser", findUserInfo);
        model.addAttribute("loginUserId", loginUserId);
        return "mypage/mypage";
    }
}
