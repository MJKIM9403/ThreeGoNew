package com.io.threegonew.repository;

import com.io.threegonew.domain.QLikes;
import com.io.threegonew.domain.Review;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.io.threegonew.domain.QLikes.likes;
import static com.io.threegonew.domain.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Review> findMyReviews(Pageable pageable, String userId) {
        List<Review> reviewList = jpaQueryFactory
                .select(review)
                .from(review)
                .where(review.user.id.eq(userId))
                .orderBy(review.reviewId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(reviewList, pageable, countMyReviews(userId));
    }

    @Override
    public Long countMyReviews(String userId) {
        Long count = jpaQueryFactory
                .select(review.count())
                .from(review)
                .where(review.user.id.eq(userId))
                .fetchOne();
        return count;
    }

    @Override
    public Page<Review> findMyLikeReviews(Pageable pageable, String userId) {
        List<Review> likeReviewList = jpaQueryFactory
                .select(review)
                .from(review)
                .join(likes)
                .on(review.reviewId.eq(likes.reviewId))
                .where(likes.userId.eq(userId))
                .orderBy(likes.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(likeReviewList, pageable, countMyLikeReviews(userId));
    }

    @Override
    public Long countMyLikeReviews(String userId) {
        Long count = jpaQueryFactory
                .select(review.count())
                .from(review)
                .join(likes)
                .on(review.reviewId.eq(likes.reviewId))
                .where(likes.userId.eq(userId))
                .fetchOne();
        return count;
    }
}
