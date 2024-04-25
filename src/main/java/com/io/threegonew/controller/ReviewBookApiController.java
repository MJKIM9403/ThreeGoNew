package com.io.threegonew.controller;

import com.io.threegonew.domain.Planner;
import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.PlannerService;
import com.io.threegonew.service.ReviewBookService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class ReviewBookApiController {
    private final UserService userService;
    private final PlannerService plannerService;
    private final ReviewBookService reviewBookService;

    @PostMapping("")
    public ResponseEntity saveReviewBook(@ModelAttribute AddReviewBookRequest request) {
        String loginUserId = userService.getCurrentUserId();
        try {
            User author = userService.findUser(loginUserId);
            Planner selectedPlanner = plannerService.findPlanner(request.getPlannerId());

            ReviewBook reviewBook = reviewBookService.createReviewBook(author, selectedPlanner, request);

            return ResponseEntity.ok().body(reviewBook.getBookId());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰북 생성에 실패했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500" , "리뷰북 생성에 실패했습니다.");
        }
    }


    @DeleteMapping("/{reviewBookId}")
    public ResponseEntity deleteReviewBook(@PathVariable Long reviewBookId) {
        String loginUserId = userService.getCurrentUserId();
        try{
            reviewBookService.deleteReviewBook(reviewBookId, loginUserId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰북 삭제에 실패했습니다.");
        }
    }

    @PutMapping("/{reviewBookId}")
    public ResponseEntity updateReviewBook(@PathVariable Long reviewBookId, @ModelAttribute AddReviewBookRequest request) {
        try{
            reviewBookService.updateReviewBook(reviewBookId, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "리뷰북 수정에 실패했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500" , "리뷰북 수정에 실패했습니다.");
        }
    }

    @GetMapping("/me/show-planner")
    public ResponseEntity showSelectList(){
        String userId = userService.getCurrentUserId();
        if(userId.equals("anonymousUser")){
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403" , "플래너 목록의 조회 권한이 없습니다.");
        }

        List<PlannerResponse> plannerList = plannerService.getCreatedOrSharedPlanners(userId);

        return ResponseEntity.ok().body(plannerList);
    }
}
