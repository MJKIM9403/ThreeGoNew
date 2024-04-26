package com.io.threegonew.controller;

import com.io.threegonew.domain.Comment;
import com.io.threegonew.dto.*;
import com.io.threegonew.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/review/{reviewId}/comment")
    public ResponseEntity saveComment(@RequestBody AddCommentRequest request){
        try{
            Comment savedComment = commentService.saveComment(request);
            return ResponseEntity.ok(savedComment.getCommentId());
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403", "댓글 작성 권한이 없습니다.");
        }
    }
    @GetMapping("/review/{reviewId}/comments")
    public ResponseEntity showComments(@PathVariable(value = "reviewId") Long reviewId,
                                                                      @RequestParam(value = "page", defaultValue = "0") Integer pageNo)
    {
        try{
            PageResponse<CommentResponse> pageResponse = commentService.getComments(reviewId, pageNo);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "댓글 목록 조회에 실패하였습니다.");
        }
    }

    @GetMapping("/review/{reviewId}/comment/{group}/replies")
    public ResponseEntity showReplies(@PathVariable(value = "reviewId") Long reviewId,
                                                                   @PathVariable(value = "group") Integer group,
                                                                   @RequestParam(value = "page", defaultValue = "0") Integer pageNo)
    {
        try{
            PageResponse<ReplyResponse> pageResponse = commentService.getReplies(reviewId, group, pageNo);
            return ResponseEntity.ok().body(pageResponse);
        }catch (Exception e) {
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "답글 목록 조회에 실패하였습니다.");
        }
    }

    @PutMapping("/comment/{commentId}")
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
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "댓글 수정에 실패하였습니다.");
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403", "댓글 수정 권한이 없습니다.");
        }
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable(value = "commentId") Long commentId) {
        try{
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "댓글 삭제에 실패하였습니다.");
        }catch (AccessDeniedException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.FORBIDDEN, "403", "댓글 삭제 권한이 없습니다.");
        }
    }

    @GetMapping("/comment/{commentId}/temp-reply")
    public ResponseEntity addTempReply(@PathVariable(value = "commentId") Long commentId){
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
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "답글 조회에 실패하였습니다.");
        }
    }

    @GetMapping("/review/{reviewId}/comments/recent")
    public ResponseEntity showRecentComments(@PathVariable(value = "reviewId") Long reviewId,
                                             @RequestParam(value = "last") Long lastCommentId)
    {
        try {
            List<CommentResponse> recentComments = new ArrayList<>();

            if(commentService.hasNewComments(reviewId, lastCommentId)){
                recentComments = commentService.getRecentComments(reviewId, lastCommentId);
            }

            return ResponseEntity.ok().body(recentComments);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "최신 댓글 목록 조회에 실패하였습니다.");
        }
    }


    @GetMapping("/review/{reviewId}/comments/count")
    public ResponseEntity showCommentCount(@PathVariable(value = "reviewId") Long reviewId){
        try{
            Long allCommentsCount = commentService.getAllCommentsCount(reviewId);
            return ResponseEntity.ok().body(allCommentsCount);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ErrorResponse.createErrorResponse(HttpStatus.BAD_REQUEST, "400" , "총 댓글 수 조회에 실패하였습니다.");
        }
    }
}
