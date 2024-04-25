package com.io.threegonew.controller;

import com.io.threegonew.domain.Comment;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Long> saveComment(@RequestBody AddCommentRequest request){
        try{
            Comment savedComment = commentService.saveComment(request);
            return ResponseEntity.ok(savedComment.getCommentId());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
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

    @GetMapping("/new-reply")
    public ResponseEntity<Map<String,Object>> addNewReply(@RequestParam Long commentId){
        try {
            Comment reply = commentService.getComment(commentId);
            ReplyResponse replyResponse = commentService.getReplyResponse(reply);
            Long replyCount = commentService.getReplyCount(reply);

            Map<String,Object> result = new HashMap<>();
            result.put("reply", replyResponse);
            result.put("replyCount", replyCount);

            return ResponseEntity.ok().body(result);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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


    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@RequestBody EditCommentRequest request) {
        try{
            Map<String, Object> result = new HashMap<>();
            Comment updatedComment = commentService.updateComment(request);
            if(updatedComment.getParent() == null){
                CommentResponse comment = commentService.getCommentResponse(updatedComment);
                result.put("type","comment");
                result.put("updatedComment",comment);
            }else {
                ReplyResponse reply = commentService.getReplyResponse(updatedComment);
                result.put("type","reply");
                result.put("updatedComment",reply);
            }
            return ResponseEntity.ok().body(result);
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
