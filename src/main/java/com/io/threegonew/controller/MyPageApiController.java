package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageApiController {
    private final TourItemService tourItemService;
    private final ReviewService reviewService;
    private final ReviewBookService reviewBookService;

    @GetMapping("/reviews")
    public ResponseEntity<MyPageResponse> getMyReview(@ModelAttribute MyPageRequest request){
        MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("review")
                        .pageResponse(reviewService.getMyReviews(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }

    @GetMapping("/reviewbooks")
    public ResponseEntity<MyPageResponse> getMyReviewBook(@ModelAttribute MyPageRequest request){
        MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("reviewbook")
                        .pageResponse(reviewBookService.findMyReviewBook(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }

    @GetMapping("/likes")
    public ResponseEntity<MyPageResponse> getMyLikeReview(@ModelAttribute MyPageRequest request){
        MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("like")
                        .pageResponse(reviewService.getMyLikeReview(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }
    @GetMapping("/bookmarks")
    public ResponseEntity<MyPageResponse> getMyBookmark(@ModelAttribute MyPageRequest request){
        MyPageResponse<PageResponse<TourItemResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("bookmark")
                        .pageResponse(tourItemService.findMyBookmark(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }


}
