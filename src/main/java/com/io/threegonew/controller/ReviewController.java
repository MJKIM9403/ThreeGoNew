package com.io.threegonew.controller;

import com.io.threegonew.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewController {
    private UserService userService;

    @GetMapping("/review")
    public String getReviews(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginUserId = "";

        if (principal.equals("anonymousUser")) {
            loginUserId = "anonymousUser";
        } else {
            UserDetails userDetails = (UserDetails) principal;
            loginUserId = userDetails.getUsername();
        }
        model.addAttribute("loginUserId", loginUserId);

        return "index2";
    }
}
