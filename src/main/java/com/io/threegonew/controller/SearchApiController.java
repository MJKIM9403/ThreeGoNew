package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchApiController {
    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/review/recommend")
    public ResponseEntity<PageResponse<SimpleReviewResponse>> showRecommendReviewByKeyword(@ModelAttribute PageWithFromDateRequest request) {
        try{
            PageResponse<SimpleReviewResponse> pageResponse = reviewService.getRecommendReviewByKeyword(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/review/recent")
    public ResponseEntity<PageResponse<SimpleReviewResponse>> showRecentReviewByKeyword(@ModelAttribute PageWithFromDateRequest request) {
        try{
            PageResponse<SimpleReviewResponse> pageResponse = reviewService.getRecentReviewByKeyword(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<PageResponse<UserInfoResponse>> showUsersByStartOrContainUserId(@ModelAttribute PageWithFromDateRequest request) {
        try{
            PageResponse<UserInfoResponse> pageResponse = userService.getUserByStartOrContainUserId(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
