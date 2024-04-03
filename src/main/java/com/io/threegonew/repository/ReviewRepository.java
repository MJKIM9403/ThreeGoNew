package com.io.threegonew.repository;

import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.ReviewBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByReviewBook(ReviewBook reviewBook);

    @Modifying
    @Query("UPDATE Review r SET r.viewCount = r.viewCount + 1 WHERE r.reviewId = :reviewId")
    int increaseViewCount(@Param("reviewId") Long reviewId);
}
