package com.io.threegonew.controller;

import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final UserService userService;
    @GetMapping("")
    public String getSearchInfo(Model model){
        UserInfoResponse loginUserInfo;

        try{
            String loginUserId = userService.getCurrentUserId();
            loginUserInfo = userService.findUserInfo(loginUserId);
        }catch (IllegalArgumentException e){
            loginUserInfo = UserInfoResponse.builder()
                    .id("anonymousUser")
                    .build();
        }

        model.addAttribute("loginUser", loginUserInfo);
        return "search/search";
    }
}
