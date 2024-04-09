package com.io.threegonew.service;

import com.io.threegonew.domain.Comment;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddCommentRequest;
import com.io.threegonew.dto.EditCommentRequest;
import com.io.threegonew.repository.CommentRepository;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment getComment(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("댓글 정보를 찾을 수 없습니다."));
    }

    private Integer getNextGroup(Long reviewId){
        return commentRepository.maxGroup(reviewId) + 1;
    }

    private Integer getNextOrder(Long reviewId, Integer group){
        return commentRepository.maxOrder(reviewId, group) +1;
    }

    @Transactional
    public Comment saveComment(AddCommentRequest request){
        String loginUserId = SecurityUtils.getCurrentUsername();
        User commentWriter = userRepository.findById(loginUserId).orElseThrow(() ->
                new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));

        Comment parentComment = null;
        Integer group;

        if(request.getParentId() == null){
            group = getNextGroup(request.getReviewId());
        }else {
            parentComment = getComment(request.getParentId());
            group = parentComment.getGroup();
        }

        Comment comment = Comment.builder()
                .reviewId(request.getReviewId())
                .writer(commentWriter)
                .content(request.getContent())
                .group(group)
                .order(getNextOrder(request.getReviewId(), group))
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = getComment(commentId);

        comment.deleteComment();
    }

    @Transactional
    public void updateComment(EditCommentRequest request){
        Comment comment = getComment(request.getCommentId());

        comment.updateComment(request.getContent());
    }


}
