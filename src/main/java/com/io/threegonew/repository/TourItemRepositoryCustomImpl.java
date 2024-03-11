package com.io.threegonew.repository;

import com.io.threegonew.domain.TourItem;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.io.threegonew.domain.QBookmark.bookmark;
import static com.io.threegonew.domain.QTourItem.tourItem;


@Repository
@RequiredArgsConstructor
public class TourItemRepositoryCustomImpl implements TourItemRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<TourItem> findMyBookmarkByAreacode(Pageable pageable, String areacode, String userId) {
        List<TourItem> contents = jpaQueryFactory
                .select(tourItem)
                .from(tourItem)
                .join(bookmark)
                .on(tourItem.contentid.eq(bookmark.tourItem.contentid))
                .where(
                        tourItem.areacode.eq(areacode),
                        bookmark.user.id.eq(userId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(contents, pageable, countMyBookmarkByAreacode(areacode, userId));
    }

    @Override
    public long countMyBookmarkByAreacode(String areacode, String userId) {
        long count = jpaQueryFactory
                .select(tourItem.count())
                .from(tourItem)
                .join(bookmark)
                .on(tourItem.contentid.eq(bookmark.tourItem.contentid))
                .where(
                        tourItem.areacode.eq(areacode),
                        bookmark.user.id.eq(userId)
                ).fetchFirst();
        return count;
    }
}
