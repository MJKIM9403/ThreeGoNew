package com.io.threegonew.repository;

import com.io.threegonew.domain.Like;
import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndReview(User user, Review review);

    Optional<Like> findByUserAndReview(User user, Review review);

}
