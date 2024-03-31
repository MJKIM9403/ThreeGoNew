package com.io.threegonew.controller;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.dto.ReviewBookResponse;
import com.io.threegonew.dto.ReviewResponse;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class ReviewBookController {
    private final ReviewBookService reviewBookService;
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("")
    public String showDetailReviewBook(@RequestParam Long bookId, Model model){
        ReviewBookResponse reviewBook = reviewBookService.findReviewBookResponse(bookId);
        ReviewBook book = reviewBookService.findReviewBook(bookId);
        List<ReviewResponse> reviewList = reviewService.findReviewByReviewBook(book);

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
        model.addAttribute("reviewBook", reviewBook);
        model.addAttribute("reviewList", reviewList);
        return "review/reviewbook";
    }


}
