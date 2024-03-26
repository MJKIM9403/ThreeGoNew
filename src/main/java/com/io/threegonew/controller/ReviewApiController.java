package com.io.threegonew.controller;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewApiController {
    private final UserService userService;
    private final TourItemService tourItemService;
    private final ReviewBookService reviewBookService;
    private final PlannerService plannerService;
    private final PlanService planService;
    private final ReviewService reviewService;

    @PostMapping("/my-review")
    public ResponseEntity<MyPageResponse> getMyReviewBook(@RequestBody MyPageRequest request){
        MyPageResponse<PageResponse<MyReviewResponse>> myPageResponse =
                MyPageResponse.builder()
                        .type("review")
                        .pageResponse(reviewService.findMyReview(request))
                        .build();

        return ResponseEntity.ok().body(myPageResponse);
    }

    @PostMapping("/create")
    public ResponseEntity saveReview(@ModelAttribute AddReviewRequest request) {
        ReviewBook selectedReviewBook = null;
        TourItem selectedTourItem = null;
        try {
            if(request.getBookId() != null){
                selectedReviewBook = reviewBookService.findReviewBook(request.getBookId());
            }
            if(request.getTouritemId() != null){
                selectedTourItem = tourItemService.findTourItem(request.getTouritemId());
            }
            User author = userService.findUser(request.getUserId());

            reviewService.saveReview(selectedReviewBook, author, selectedTourItem, request);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteReview(@RequestParam Long reviewId){
        try {
            reviewService.deleteReview(reviewId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateReview(@ModelAttribute UpdateReviewRequest request){
        System.out.println(request);
        ReviewBook selectedReviewBook = null;
        TourItem selectedTourItem = null;
        try {
            if(request.getBookId() != null){
                selectedReviewBook = reviewBookService.findReviewBook(request.getBookId());
            }
            if(request.getTouritemId() != null){
                selectedTourItem = tourItemService.findTourItem(request.getTouritemId());
            }

            reviewService.updateReview(selectedReviewBook, selectedTourItem, request);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/show_list")
    public ResponseEntity<Map<String, Object>> showSelectList(){
        String userId = userService.getCurrentUserId();
        User loginUser = userService.findUser(userId);
        List<PlannerResponse> plannerList = plannerService.findMyPlannerList(userId);
        List<ReviewBookResponse> reviewBookList = reviewBookService.findMyReviewBookList(loginUser);
        Map<String, Object> result = new HashMap<>();
        result.put("plannerList", plannerList);
        result.put("reviewBookList", reviewBookList);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/show_plan")
    public ResponseEntity<Map<String, Object>> showPlanList(@RequestParam("bookId") Long bookId){
        Long plannerId = reviewBookService.findReviewBook(bookId)
                .getPlanner()
                .getPlannerId();

        List<SelectPlanResponse> planList = planService.findPlanListByPlannerId(plannerId);

        Map<String, Object> result = new HashMap<>();
        result.put("planList", planList);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/detail")
    public ResponseEntity<ReviewResponse> showDetailReview(@RequestParam("reviewId") Long reviewId){
        try{
            ReviewResponse findReview = reviewService.findDetailReview(reviewId);
            return ResponseEntity.ok().body(findReview);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/simple-touritem")
    public ResponseEntity<TourItemSimpleResponse> showTourItemSimpleInfo(@RequestParam("tourItemId") String tourItemId){
        TourItemSimpleResponse tourItemSimpleInfo;

        try{
            tourItemSimpleInfo = tourItemService.findTourItemSimpleInfo(tourItemId);
        }catch (IllegalArgumentException e){
            tourItemSimpleInfo = TourItemSimpleResponse.builder()
                    .title("등록된 관광지 정보가 없습니다.")
                    .fullCategoryName("-")
                    .address("-")
                    .firstimage("../assets/img/no_img.jpg")
                    .build();
        }
        return ResponseEntity.ok().body(tourItemSimpleInfo);
    }
}
