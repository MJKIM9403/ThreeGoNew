package com.io.threegonew.controller;

import com.io.threegonew.dto.AddReviewRequest;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private UserService userService;

    @GetMapping("")
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

//    @PostMapping("/create")
//    @ResponseBody
//    public Long saveReview(AddReviewRequest request) {
//
//    }
}
