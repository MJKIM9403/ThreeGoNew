package com.io.threegonew.repository;

import com.io.threegonew.domain.ReviewBook;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.io.threegonew.domain.QReviewBook.reviewBook;

@Repository
@RequiredArgsConstructor
public class ReviewBookRepositoryCustomImpl implements ReviewBookRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<ReviewBook> findMyReviewBook(Pageable pageable, String userId) {
        List<ReviewBook> reviewBookList = jpaQueryFactory
                .select(reviewBook)
                .from(reviewBook)
                .where(reviewBook.user.id.eq(userId))
                .orderBy(reviewBook.bookId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(reviewBookList, pageable, countMyReview(userId));
    }

    @Override
    public Long countMyReview(String userId) {
        Long count = jpaQueryFactory
                .select(reviewBook.count())
                .from(reviewBook)
                .where(reviewBook.user.id.eq(userId))
                .fetchFirst();
        return count;
    }
}
