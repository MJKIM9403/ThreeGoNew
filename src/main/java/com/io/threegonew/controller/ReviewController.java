package com.io.threegonew.controller;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final UserService userService;
    private final TourItemService tourItemService;
    private final ReviewBookService reviewBookService;
    private final PlannerService plannerService;
    private final PlanService planService;
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

        return "review/review";
    }

    @GetMapping("/edit/{reviewId}")
    public String editReview(@PathVariable Long reviewId, Model model){
        String userId = userService.getCurrentUserId();
        User loginUser = userService.findUser(userId);

        EditReviewResponse reviewResponse = reviewService.findEditReview(reviewId);

        List<ReviewBookResponse> reviewBookList = reviewBookService.findMyReviewBookList(loginUser);;
        List<SelectPlanResponse> planList;

        if(reviewResponse.getReviewBookId() != null){
            Long plannerId = reviewBookService.findReviewBook(reviewResponse.getReviewBookId())
                    .getPlanner()
                    .getPlannerId();
            planList = planService.findPlanListByPlannerId(plannerId);
        }else {
            planList = new ArrayList<>();
        }

        model.addAttribute("reviewBookList", reviewBookList);
        model.addAttribute("planList", planList);
        model.addAttribute("reviewResponse", reviewResponse);
        return "review/editreview";
    }

    @PostMapping("/create")
    @ResponseBody
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

    @GetMapping("/show_list")
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<ReviewResponse> showDetailReview(@RequestParam("reviewId") Long reviewId){
        try{
            ReviewResponse findReview = reviewService.findDetailReview(reviewId);
            return ResponseEntity.ok().body(findReview);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/simple-touritem")
    @ResponseBody
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
