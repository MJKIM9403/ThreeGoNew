package com.io.threegonew.controller;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.UserService;
import com.io.threegonew.util.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class ReviewBookApiController {
    private final UserService userService;
    private final PlannerService plannerService;
    private final ReviewBookService reviewBookService;

    @PostMapping("/create")
    public ResponseEntity<Long> saveReviewBook(@ModelAttribute AddReviewBookRequest request) {
        String loginUserId = userService.getCurrentUserId();
        try {
            User author = userService.findUser(loginUserId);
            Planner selectedPlanner = plannerService.findPlanner(request.getPlannerId());

            ReviewBook reviewBook = reviewBookService.createReviewBook(author,selectedPlanner,request);

            return ResponseEntity.ok().body(reviewBook.getBookId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{reviewBookId}")
    public ResponseEntity deleteReviewBook(@PathVariable Long reviewBookId) {
        String loginUserId = userService.getCurrentUserId();
        try{
            reviewBookService.deleteReviewBook(reviewBookId, loginUserId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewBookId}")
    public ResponseEntity updateReviewBook(@PathVariable Long reviewBookId, @ModelAttribute AddReviewBookRequest request) {
        System.out.println(request);
        try{
            reviewBookService.updateReviewBook(reviewBookId, request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/show_planner")
    public ResponseEntity<List<PlannerResponse>> showSelectList(){
        String userId = userService.getCurrentUserId();
        List<PlannerResponse> plannerList = plannerService.getCreatedOrSharedPlanners(userId);

        return ResponseEntity.ok().body(plannerList);
    }
}
