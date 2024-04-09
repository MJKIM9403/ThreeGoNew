package com.io.threegonew.controller;

import com.io.threegonew.dto.AddCommentRequest;
import com.io.threegonew.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/comment")
@RestController
public class CommentApiController {
    private final CommentService commentService;

//    @PostMapping("")
//    public ResponseEntity saveComment(@RequestBody AddCommentRequest request){
//
//    }
}
