package com.io.threegonew.controller;


import com.io.threegonew.domain.Likes;
import com.io.threegonew.dto.LikesResponse;
import com.io.threegonew.service.LikesService;
import com.io.threegonew.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    .LikeCount(likesService.getLikeCount(reviewId))
                    .LikeState(false)
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
                .LikeCount(likesService.getLikeCount(reviewId))
                .LikeState(likesService.getLikeState(loginUserId, reviewId))
                .build());

    }

}
