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

import java.nio.file.AccessDeniedException;
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

    @PostMapping("/create")
    public ResponseEntity saveReview(@ModelAttribute AddReviewRequest request) {
        ReviewBook selectedReviewBook = null;
        TourItem selectedTourItem = null;
        String loginUserId = userService.getCurrentUserId();
        try {
            if(request.getBookId() != null){
                selectedReviewBook = reviewBookService.findReviewBook(request.getBookId());
            }
            if(request.getTouritemId() != null){
                selectedTourItem = tourItemService.findTourItem(request.getTouritemId());
            }
            User author = userService.findUser(loginUserId);

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

    @PutMapping("/view_count")
    public ResponseEntity<Long> viewCountUp(@RequestParam("reviewId") Long reviewId){
        try{
            int updateState = reviewService.viewCountUp(reviewId);
            if(updateState > 0){
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/show_list")
    public ResponseEntity<Map<String, Object>> showSelectList(){
        String userId = userService.getCurrentUserId();
        User loginUser = userService.findUser(userId);
        List<PlannerResponse> plannerList = plannerService.findMyPlannerList(userId);
        List<ReviewBookResponse> reviewBookList = reviewBookService.findReviewBookByUser(loginUser);
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
            ReviewResponse findReview = reviewService.getDetailReview(reviewId);
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

    @GetMapping("/recommend")
    public ResponseEntity<PageResponse> showRecommendReview(@ModelAttribute RecommendReviewRequest request){
        try{
            PageResponse<SimpleReviewResponse> pageResponse = reviewService.getRecommendReview(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/follow")
    public ResponseEntity<PageResponse> showFollowReview(@ModelAttribute MyPageRequest request){
        try{
            PageResponse<SimpleReviewResponse> pageResponse = reviewService.getFollowReview(request);
            return ResponseEntity.ok().body(pageResponse);
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
