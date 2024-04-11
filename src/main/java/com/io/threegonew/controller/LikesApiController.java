package com.io.threegonew.controller;


import com.io.threegonew.dto.*;
import com.io.threegonew.service.LikesService;
import com.io.threegonew.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/likes")
@RestController
public class LikesApiController {
    private final LikesService likesService;

    @PostMapping("/{reviewId}")
    public ResponseEntity<LikesResponse> checkLike(@PathVariable(value = "reviewId") Long reviewId){
        String loginUserId = SecurityUtils.getCurrentUsername();
        if(loginUserId.equals("anonymousUser")){
            return ResponseEntity.ok(LikesResponse.builder()
                    .likeCount(likesService.getLikeCount(reviewId))
                    .likeState(false)
                    .build());
        }else {
            boolean isChecked = likesService.getLikeState(loginUserId, reviewId);
            if(isChecked){
                likesService.deleteLike(loginUserId, reviewId);
            }else {
                try {
                    likesService.addLike(loginUserId, reviewId);
                }catch (Exception e){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return ResponseEntity.ok(LikesResponse.builder()
                .likeCount(likesService.getLikeCount(reviewId))
                .likeState(likesService.getLikeState(loginUserId, reviewId))
                .build());

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<LikesResponse> showLike(@PathVariable(value = "reviewId") Long reviewId){
        String loginUserId = SecurityUtils.getCurrentUsername();
        if(loginUserId.equals("anonymousUser")) {
            return ResponseEntity.ok(LikesResponse.builder()
                    .likeCount(likesService.getLikeCount(reviewId))
                    .likeState(false)
                    .build());
        }else {
            return ResponseEntity.ok(LikesResponse.builder()
                    .likeCount(likesService.getLikeCount(reviewId))
                    .likeState(likesService.getLikeState(loginUserId, reviewId))
                    .build());
        }
    }
}
