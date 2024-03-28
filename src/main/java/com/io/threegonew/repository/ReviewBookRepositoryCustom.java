package com.io.threegonew.repository;

import com.io.threegonew.domain.ReviewBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewBookRepositoryCustom {
    Page<ReviewBook> findMyReviewBook(Pageable pageable, String userId);

    Long countMyReview(String userId);
}
