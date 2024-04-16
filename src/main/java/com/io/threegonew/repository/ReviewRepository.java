package com.io.threegonew.repository;

import com.io.threegonew.domain.Review;
import com.io.threegonew.domain.ReviewBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByReviewBook(ReviewBook reviewBook);

    @Modifying
    @Query("UPDATE Review r SET r.viewCount = r.viewCount + 1 WHERE r.reviewId = :reviewId")
    int increaseViewCount(@Param("reviewId") Long reviewId);

    @Query(value = "SELECT r.* " +
            "FROM review r " +
            "LEFT JOIN likes l " +
            "ON r.review_id = l.review_id " +
            "GROUP BY r.review_id " +
            "ORDER BY " +
            "SUM(CASE WHEN l.reg_date BETWEEN :toDate AND :fromDate THEN 1 ELSE 0 END) DESC, " +
            "SUM(CASE WHEN l.reg_date <= :fromDate THEN 1 ELSE 0 END) DESC, " +
            "r.review_id DESC ",
            countQuery = "SELECT count(r.review_id) FROM review r",
            nativeQuery = true)
    Page<Review> findRecommendReviews(Pageable pageable, @Param("toDate")LocalDateTime toDate, @Param("fromDate")LocalDateTime fromDate);

    @Query(value = "SELECT r.* " +
                "FROM review r " +
                "INNER JOIN follows f " +
                "ON r.user_id = f.from_user_id " +
                "WHERE f.to_user_id = :userId " +
                "ORDER BY r.review_id DESC",
            countQuery = "SELECT count(r.review_id) " +
                    "FROM review r " +
                    "INNER JOIN follows f " +
                    "ON r.user_id = f.from_user_id " +
                    "WHERE f.to_user_id = :userId ",
            nativeQuery = true)
    Page<Review> findFollowReview(Pageable pageable, @Param("userId")String userId);
}
