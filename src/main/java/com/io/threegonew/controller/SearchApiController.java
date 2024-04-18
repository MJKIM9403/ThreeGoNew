package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/id")
    public ResponseEntity<PageResponse<UserWithFollowStateResponse>> showUsersByStartOrContainUserId(@RequestParam(name = "keyword") String keyword,
                                                                                                     @RequestParam(name = "page") int page)
    {
        try{
            PageResponse<UserWithFollowStateResponse> pageResponse = userService.getUserByStartOrContainUserId(keyword, page);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/name")
    public ResponseEntity<PageResponse<UserWithFollowStateResponse>> showUsersByStartOrContainUserName(@RequestParam(name = "keyword") String keyword,
                                                                                                     @RequestParam(name = "page") int page)
    {
        try{
            PageResponse<UserWithFollowStateResponse> pageResponse = userService.getUserByStartOrContainUserName(keyword, page);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
