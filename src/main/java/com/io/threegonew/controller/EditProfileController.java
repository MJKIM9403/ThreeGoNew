package com.io.threegonew.controller;

import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EditProfileController {

    private final UserService userService;

    @GetMapping("/editprofile")
    public String getEditprofile(){
        return "editprofile";
    }

}
