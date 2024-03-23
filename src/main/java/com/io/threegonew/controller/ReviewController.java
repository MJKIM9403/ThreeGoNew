package com.io.threegonew.controller;

import com.io.threegonew.domain.Planner;
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
import org.springframework.web.multipart.MultipartFile;

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

        return "index2";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity saveReview(@RequestParam("bookId") Long bookId,
                                     @RequestParam("userId") String userId,
                                     @RequestParam("touritemId") String touritemId,
                                     @RequestParam("touritemTitle") String touritemTitle,
                                     @RequestParam("reviewContent") String reviewContent,
                                     @RequestParam(name = "photoList") List<MultipartFile> photoList) {

        AddReviewRequest request = AddReviewRequest.builder()
                .bookId(bookId)
                .userId(userId)
                .touritemId(touritemId)
                .touritemTitle(touritemTitle)
                .reviewContent(reviewContent)
                .photoList(photoList)
                .build();

        try {
            ReviewBook selectedReviewBook = reviewBookService.findReviewBook(request.getBookId());
            User author = userService.findUser(request.getUserId());
            TourItem selectedTourItem = tourItemService.findTourItem(request.getTouritemId());

            reviewService.saveReview(selectedReviewBook, author, selectedTourItem, request);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/show_list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showSelectList(@RequestParam("userId") String userId){
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
}
