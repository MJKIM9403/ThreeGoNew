package com.io.threegonew.service;

import com.io.threegonew.domain.Comment;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.CommentRepository;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

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

        if(parentComment != null){
            comment.setParent(parentComment);
        }

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) throws AccessDeniedException{
        Comment comment = getComment(commentId);
        String loginUserId = SecurityUtils.getCurrentUsername();
        if(loginUserId.equals(comment.getWriter().getId())){
            comment.deleteComment();
        }else {
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }
    }

    @Transactional
    public void updateComment(EditCommentRequest request) throws AccessDeniedException {
        Comment comment = getComment(request.getCommentId());
        String loginUserId = SecurityUtils.getCurrentUsername();
        if(loginUserId.equals(comment.getWriter().getId())){
            comment.updateComment(request.getContent());
        }else {
            throw new AccessDeniedException("댓글 업데이트 권한이 없습니다.");
        }
    }

    @Transactional
    public PageResponse getComments(CommentRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<CommentResponse> page = commentRepository.findComments(pageable, request.getReviewId())
                .map(this::commentMapper);

        PageResponse<CommentResponse> pageResponse = PageResponse.<CommentResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    @Transactional
    public PageResponse getReplies(CommentRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<ReplyResponse> page = commentRepository.findReplies(pageable, request.getReviewId(), request.getGroup())
                .map(this::replyMapper);

        PageResponse<ReplyResponse> pageResponse = PageResponse.<ReplyResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
    }

    private CommentResponse commentMapper(Comment comment){
        Integer childrenCount = commentRepository.countReplies(comment.getReviewId(), comment.getGroup()).intValue();

        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .writer(userInfoMapper(comment.getWriter()))
                .content(comment.getContent())
                .group(comment.getGroup())
                .childrenCount(childrenCount)
                .cmtDel(comment.getCmtDel())
                .build();
    }

    private ReplyResponse replyMapper(Comment reply) {
        return ReplyResponse.builder()
                .commentId(reply.getCommentId())
                .writer(userInfoMapper(reply.getWriter()))
                .content(reply.getContent())
                .group(reply.getGroup())
                .parentId(reply.getParent().getCommentId())
                .patentWriterId(reply.getParent().getWriter().getId())
                .cmtDel(reply.getCmtDel())
                .build();
    }

    private UserInfoResponse userInfoMapper(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getU_sfile())
                .about(user.getAbout())
                .build();
    }
}
