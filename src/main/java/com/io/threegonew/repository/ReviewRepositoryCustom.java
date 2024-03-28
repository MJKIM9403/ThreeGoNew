package com.io.threegonew.repository;

import com.io.threegonew.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> findMyReview(Pageable pageable, String userId);

    Long countMyReviewBook(String userId);
}
