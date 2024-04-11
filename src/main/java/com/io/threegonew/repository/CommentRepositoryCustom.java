package com.io.threegonew.repository;

import com.io.threegonew.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepositoryCustom {
    Integer maxGroup(Long reviewId);

    Integer maxOrder(Long reviewId, Integer group);

    Page<Comment> findComments(Pageable pageable, Long reviewId);

    Long countComments(Long reviewId);

    List<Comment> findRecentComments(Long reviewId, LocalDateTime lastRegDate);

    Boolean existNewComments(Long reviewId, LocalDateTime lastRegDate);

    Page<Comment> findReplies(Pageable pageable, Long reviewId, Integer group);

    Long countReplies(Long reviewId, Integer group);
}
