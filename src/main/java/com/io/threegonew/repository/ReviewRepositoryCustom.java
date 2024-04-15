package com.io.threegonew.repository;

import com.io.threegonew.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReviewRepositoryCustom {
    Page<Review> findMyReviews(Pageable pageable, String userId);

    Long countMyReviews(String userId);

    Page<Review> findMyLikeReviews(Pageable pageable, String userId);

    Long countMyLikeReviews(String userId);

}
