package com.io.threegonew.controller;


import com.io.threegonew.dto.*;
import com.io.threegonew.service.LikesService;
import com.io.threegonew.commons.SecurityUtils;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/api/like")
@RestController
public class LikesApiController {
    private final LikesService likesService;

    @PostMapping("/{reviewId}")
    public ResponseEntity checkLike(@PathVariable(value = "reviewId") Long reviewId){
        try{
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
                    likesService.addLike(loginUserId, reviewId);
                }
                return ResponseEntity.ok(LikesResponse.builder()
                        .likeCount(likesService.getLikeCount(reviewId))
                        .likeState(likesService.getLikeState(loginUserId, reviewId))
                        .build());
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400", "관심등록 정보를 확인할 수 없습니다.");
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403", "관심등록 삭제 권한이 없습니다.");
        }catch (DuplicateRequestException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.CONFLICT, "409", "이미 존재하는 요청입니다.");
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity showLike(@PathVariable(value = "reviewId") Long reviewId){
        try {
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
        }catch (RuntimeException e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "관심등록 정보를 확인할 수 없습니다.");
        }

    }
}
