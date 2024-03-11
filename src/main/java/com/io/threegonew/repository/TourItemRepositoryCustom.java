package com.io.threegonew.repository;

import com.io.threegonew.domain.TourItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TourItemRepositoryCustom {
    Page<TourItem> findMyBookmarkByAreacode(Pageable pageable, String areacode, String userId);
}
