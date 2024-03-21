package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageApiController {
    private final TourItemService tourItemService;
    private final ReviewService reviewService;

    @PostMapping("/bookmark")
    public ResponseEntity<MyPageResponse> getMyBookmark(@RequestBody MyPageRequest request){
        MyPageResponse<PageResponse<TourItemResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("bookmark")
                        .pageResponse(tourItemService.findMyBookmark(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }

    @PostMapping("/review")
    public ResponseEntity<MyPageResponse> getMyReviewBook(@RequestBody MyPageRequest request){
        MyPageResponse<PageResponse<ReviewResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("review")
                        .pageResponse(reviewService.findMyReview(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }
}
