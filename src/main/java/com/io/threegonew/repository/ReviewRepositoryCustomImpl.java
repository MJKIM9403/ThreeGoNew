package com.io.threegonew.repository;

import com.io.threegonew.domain.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.io.threegonew.domain.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Review> findMyReview(Pageable pageable, String userId) {
        List<Review> reviewList = jpaQueryFactory
                .select(review)
                .from(review)
                .where(review.user.id.eq(userId))
                .orderBy(review.reviewId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(reviewList, pageable, countMyReview(userId));
    }

    @Override
    public Long countMyReview(String userId) {
        Long count = jpaQueryFactory
                .select(review.count())
                .from(review)
                .where(review.user.id.eq(userId))
                .fetchFirst();
        return count;
    }
}
