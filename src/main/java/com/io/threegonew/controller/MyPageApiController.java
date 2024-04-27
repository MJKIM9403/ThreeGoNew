package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.ReviewService;
import com.io.threegonew.service.TourItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage/{userId}")
@RequiredArgsConstructor
public class MyPageApiController {
    private final TourItemService tourItemService;
    private final ReviewService reviewService;
    private final ReviewBookService reviewBookService;

    @GetMapping("/reviews")
    public ResponseEntity getMyReview(@ModelAttribute MyPageRequest request){
        try{
            MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                    MyPageResponse.builder()
                            .type("review")
                            .pageResponse(reviewService.getMyReviews(request))
                            .build();

            return ResponseEntity.ok().body(myPageResponse);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "리뷰 목록을 조회할 수 없습니다.");
        }
    }

    @GetMapping("/books")
    public ResponseEntity getMyReviewBook(@ModelAttribute MyPageRequest request){
        try{
            MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                    MyPageResponse.builder()
                            .type("reviewbook")
                            .pageResponse(reviewBookService.getMyReviewBook(request))
                            .build();

            return ResponseEntity.ok().body(myPageResponse);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "리뷰북 목록을 조회할 수 없습니다.");
        }
    }

    @GetMapping("/likes")
    public ResponseEntity getMyLikeReview(@ModelAttribute MyPageRequest request){
        try{
            MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                    MyPageResponse.builder()
                            .type("like")
                            .pageResponse(reviewService.getMyLikeReview(request))
                            .build();

            return ResponseEntity.ok().body(myPageResponse);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "관심리뷰 목록을 조회할 수 없습니다.");
        }
    }
    @GetMapping("/bookmarks")
    public ResponseEntity getMyBookmark(@ModelAttribute MyPageRequest request){
        try{
            MyPageResponse<PageResponse<TourItemResponse>> myPageResponse =
                    MyPageResponse.builder()
                            .type("bookmark")
                            .pageResponse(tourItemService.findMyBookmark(request))
                            .build();

            return ResponseEntity.ok().body(myPageResponse);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "북마크 목록을 조회할 수 없습니다.");
        }
    }


}
