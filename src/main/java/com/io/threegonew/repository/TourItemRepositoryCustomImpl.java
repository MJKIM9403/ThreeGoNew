package com.io.threegonew.repository;

import com.io.threegonew.domain.QTourItem;
import com.io.threegonew.domain.TourItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


//@Repository
//@RequiredArgsConstructor
//public class TourItemRepositoryCustomImpl implements TourItemRepositoryCustom{
//    private final JPAQueryFactory jpaQueryFactory;
//    @Override
//    public Page<TourItem> findMyBookmarkByAreacode(Pageable pageable, String areacode) {
//        return jpaQueryFactory
//                .select(new QTourItem(
//
//                ))
//    }
//}
