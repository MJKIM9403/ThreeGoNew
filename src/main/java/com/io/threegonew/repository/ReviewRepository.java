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
            "INNER JOIN ( " +
            "SELECT l.review_id, count(l.like_id) as like_count " +
            "FROM likes l " +
            "WHERE l.reg_date BETWEEN :toDate AND :fromDate " +
            "GROUP BY l.review_id ) as sub " +
            "ON r.review_id = sub.review_id " +
            "ORDER BY sub.like_count DESC, r.review_id ASC ",
            countQuery = "SELECT count(r.review_id) FROM review r",
            nativeQuery = true)
    Page<Review> findRecommendReviews(Pageable pageable, @Param("toDate")LocalDateTime toDate, @Param("fromDate")LocalDateTime fromDate);

    @Query(value = "SELECT r.* " +
                "FROM review r " +
                "WHERE r.user_id in ( " +
                "SELECT f.from_user_id " +
                "FROM follows f " +
                "WHERE f.to_user_id = :userId " +
                ")" +
                "ORDER BY r.review_id",
            countQuery = "SELECT count(r.review_id) " +
                    "FROM review r " +
                    "WHERE r.user_id in ( " +
                    "SELECT f.from_user_id " +
                    "FROM follows f " +
                    "WHERE f.to_user_id = :userId " +
                    ")",
            nativeQuery = true)
    Page<Review> findFollowReview(Pageable pageable, @Param("userId")String userId);
}
