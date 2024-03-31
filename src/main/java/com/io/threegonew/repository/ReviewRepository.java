package com.io.threegonew.repository;

import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.ReviewBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByReviewBook(ReviewBook reviewBook);
}
