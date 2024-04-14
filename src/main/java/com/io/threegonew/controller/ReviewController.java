package com.io.threegonew.controller;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final UserService userService;
    private final ReviewBookService reviewBookService;
    private final PlanService planService;
    private final ReviewService reviewService;

    @GetMapping("")
    public String getReviews(Model model) {
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


        return "review/review";
    }

    @GetMapping("/edit/{reviewId}")
    public String editReview(@PathVariable Long reviewId, Model model){
        String userId = userService.getCurrentUserId();
        User loginUser = userService.findUser(userId);

        EditReviewResponse reviewResponse = reviewService.getEditReview(reviewId);

        List<ReviewBookResponse> reviewBookList = reviewBookService.findReviewBookByUser(loginUser);;
        List<SelectPlanResponse> planList;

        if(reviewResponse.getReviewBookId() != null){
            Long plannerId = reviewBookService.findReviewBook(reviewResponse.getReviewBookId())
                    .getPlanner()
                    .getPlannerId();
            planList = planService.findPlanListByPlannerId(plannerId);
        }else {
            planList = new ArrayList<>();
        }

        model.addAttribute("loginUserId", userId);
        model.addAttribute("reviewId", reviewId);
        model.addAttribute("reviewBookList", reviewBookList);
        model.addAttribute("planList", planList);
        model.addAttribute("reviewResponse", reviewResponse);
        return "review/editreview";
    }
}
