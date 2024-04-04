package com.io.threegonew.repository;

import com.io.threegonew.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserIdAndReviewId(String userId, Long reviewId);

    Optional<Likes> findByUserIdAndReviewId(String userId, Long reviewId);

    Long countByReviewId(Long reviewId);
}
