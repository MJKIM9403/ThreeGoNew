package com.io.threegonew.controller;

import com.io.threegonew.dto.ReviewBookResponse;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class ReviewBookController {
    private final ReviewBookService reviewBookService;

    @GetMapping("")
    public String showDetailReviewBook(@RequestParam Long bookId, Model model){
        ReviewBookResponse reviewBook = reviewBookService.findReviewBookResponse(bookId);
        model.addAttribute("reviewBook", reviewBook);
        return "review/reviewbook";
    }


}
