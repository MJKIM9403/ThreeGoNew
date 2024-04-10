package com.io.threegonew.controller;

import com.io.threegonew.dto.*;
import com.io.threegonew.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/api/comment")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<PageResponse<CommentResponse>> showComments(CommentRequest request){
        PageResponse<CommentResponse> pageResponse = commentService.getComments(request);
        return ResponseEntity.ok().body(pageResponse);
    }

    @GetMapping("/{reviewId}/{group}")
    public ResponseEntity<PageResponse<ReplyResponse>> showReplies(CommentRequest request){
        PageResponse<ReplyResponse> pageResponse = commentService.getReplies(request);
        return ResponseEntity.ok().body(pageResponse);
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
    public ResponseEntity updateComment(@PathVariable(value = "commentId") Long commentId,
                                        @RequestBody EditCommentRequest request) {
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
}
