package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<PageResponse<CommentResponse>> showComments(@ModelAttribute CommentRequest request){
        PageResponse<CommentResponse> pageResponse = commentService.getComments(request);
        return ResponseEntity.ok().body(pageResponse);
    }

    @GetMapping("/replies")
    public ResponseEntity<PageResponse<ReplyResponse>> showReplies(@ModelAttribute CommentRequest request){
        PageResponse<ReplyResponse> pageResponse = commentService.getReplies(request);
        return ResponseEntity.ok().body(pageResponse);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CommentResponse>> showRecentComments(@RequestParam(value = "reviewId") Long reviewId,
                                                                    @RequestParam(value = "lastCommentId") Long lastCommentId){
        List<CommentResponse> recentComments = new ArrayList<>();
        try {
            if(commentService.hasNewComments(reviewId, lastCommentId)){
                recentComments = commentService.getRecentComments(reviewId, lastCommentId);
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(recentComments);
    }

    @PostMapping("")
    public ResponseEntity saveComment(@RequestBody AddCommentRequest request){
        try{
            commentService.saveComment(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@RequestBody EditCommentRequest request) {
        try{
            commentService.updateComment(request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable(value = "commentId") Long commentId) {
        try{
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/count")
    public ResponseEntity showCommentCount(@RequestParam(value = "reviewId") Long reviewId){
        try{
            Long allCommentsCount = commentService.getAllCommentsCount(reviewId);
            return ResponseEntity.ok().body(allCommentsCount);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
