package com.io.threegonew.repository;

import com.io.threegonew.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Integer maxGroup(Long reviewId);

    Integer maxOrder(Long reviewId, Integer group);

    Page<Comment> findComments(Pageable pageable, Long reviewId);

    Long countComments(Long reviewId);

    Page<Comment> findReplies(Pageable pageable, Long reviewId, Integer group);

    Long countReplies(Long reviewId, Integer group);
}
