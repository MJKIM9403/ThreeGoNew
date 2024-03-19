package com.io.threegonew.controller;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddReviewRequest;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.TourItemService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final UserService userService;
    private final TourItemService tourItemService;
    private final ReviewBookService reviewBookService;
    private final ReviewService reviewService;

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

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity saveReview(@RequestBody AddReviewRequest request) {
        try {
            ReviewBook selectedReviewBook = reviewBookService.findReviewBook(request.getBookId());
            User author = userService.findUser(request.getUserId());
            TourItem selectedTourItem = tourItemService.findTourItem(request.getTouritemId());

            reviewService.saveReview(selectedReviewBook, author, selectedTourItem, request);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
