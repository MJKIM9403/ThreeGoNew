package com.io.threegonew.controller;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewApiController {
    private final UserService userService;
    private final TourItemService tourItemService;
    private final ReviewBookService reviewBookService;
    private final PlannerService plannerService;
    private final PlanService planService;
    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity saveReview(@ModelAttribute AddReviewRequest request) {
        ReviewBook selectedReviewBook = null;
        TourItem selectedTourItem = null;
        String loginUserId = userService.getCurrentUserId();
        try {
            if (request.getBookId() != null) {
                selectedReviewBook = reviewBookService.findReviewBook(request.getBookId());
            }
            if (request.getTouritemId() != null) {
                selectedTourItem = tourItemService.findTourItem(request.getTouritemId());
            }
            User author = userService.findUser(loginUserId);

            reviewService.saveReview(selectedReviewBook, author, selectedTourItem, request);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "리뷰 저장에 실패했습니다.");
        } catch (IOException e) {
            return ErrorResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "리뷰 저장에 실패했습니다.");
        }
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity showDetailReview(@PathVariable(value = "reviewId") Long reviewId){
        try{
            ReviewResponse findReview = reviewService.getDetailReview(reviewId);
            return ResponseEntity.ok().body(findReview);
        }catch (IllegalArgumentException e){
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "리뷰 조회에 실패했습니다.");
        }
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable(value = "reviewId") Long reviewId){
        try {
            reviewService.deleteReview(reviewId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "리뷰 삭제에 실패했습니다.");
        }
    }

    @PutMapping("/review/{reviewId}")
    public ResponseEntity updateReview(@ModelAttribute UpdateReviewRequest request){
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰 수정에 실패하였습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500" , "리뷰 수정에 실패하였습니다.");
        }
    }

    @PutMapping("/review/{reviewId}/view-count")
    public ResponseEntity viewCountUp(@PathVariable(value = "reviewId") Long reviewId){
        try{
            int updateState = reviewService.viewCountUp(reviewId);
            if(updateState > 0){
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰 수정에 실패하였습니다.");
            }
        }catch (Exception e){
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰 수정에 실패하였습니다.");
        }
    }

    @GetMapping("/review/me/show-list")
    public ResponseEntity<Map<String, Object>> showSelectList(){
        String userId = userService.getCurrentUserId();
        User loginUser = userService.findUser(userId);
        if(userId.equals("anonymousUser")){
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403" , "리뷰북 목록의 조회 권한이 없습니다.");
        }
        List<PlannerResponse> plannerList = plannerService.getCreatedOrSharedPlanners(userId);
        List<ReviewBookResponse> reviewBookList = reviewBookService.findReviewBookByUser(loginUser);
        Map<String, Object> result = new HashMap<>();
        result.put("plannerList", plannerList);
        result.put("reviewBookList", reviewBookList);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/review/me/show-plan")
    public ResponseEntity<Map<String, Object>> showPlanList(@RequestParam("bookid") Long bookId){
        try{
            Long plannerId = reviewBookService.findReviewBook(bookId)
                    .getPlanner()
                    .getPlannerId();

            List<SelectPlanResponse> planList = planService.findPlanListByPlannerId(plannerId);

            Map<String, Object> result = new HashMap<>();
            result.put("planList", planList);

            return ResponseEntity.ok().body(result);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰북 정보를 조회할 수 없습니다.");
        }
    }

    @GetMapping("/review/simple-touritem/{tourItemId}")
    public ResponseEntity showTourItemSimpleInfo(@PathVariable(value = "tourItemId") String tourItemId){
        try{
            TourItemSimpleResponse tourItemSimpleInfo = tourItemService.findTourItemSimpleInfo(tourItemId);
            return ResponseEntity.ok().body(tourItemSimpleInfo);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "관광지 정보를 조회할 수 없습니다.");
        }
    }

    @GetMapping("/reviews/me/recommend")
    public ResponseEntity showRecommendReview(@ModelAttribute PageWithFromDateRequest request){
        try{
            PageResponse<SimpleReviewResponse> pageResponse = reviewService.getRecommendReview(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "추천 리뷰 목록 조회에 실패하였습니다.");
        }
    }

    @GetMapping("/reviews/me/follow")
    public ResponseEntity showFollowReview(@ModelAttribute PageWithFromDateRequest request){
        try{
            PageResponse<SimpleReviewResponse> pageResponse = reviewService.getFollowReview(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403" , "팔로우 리뷰 목록의 조회 권한이 없습니다.");
        }
    }
}
