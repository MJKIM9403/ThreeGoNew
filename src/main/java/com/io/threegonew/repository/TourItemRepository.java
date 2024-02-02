package com.io.threegonew.repository;

import com.io.threegonew.domain.Interface.TourItemInterface;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.TourItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourItemRepository extends JpaRepository<TourItem, String>, JpaSpecificationExecutor<TourItem> {
    Page<TourItem> findAll(Specification spec, Pageable pageable);

    @Query(value = "SELECT t.contentid, t.title, t.firstimage " +
            "    ( 6371 * acos( cos( radians(:#{#response.mapy}) ) * cos( radians( t.mapy ) ) " +
            "    * cos( radians( :#{#response.mapx} ) - radians(t.mapx) ) " +
            "    + sin( radians(:#{#response.mapy}) ) * sin( radians( t.mapy ) ) ) ) AS distance " +
            "FROM touritem t " +
            "where cat2 = :#{#response.cat2} " +
            "HAVING distance < 10 " +
            "ORDER BY distance " +
            "LIMIT 0 , 5;", nativeQuery = true)
    List<TourItemInterface> findTourItemByDistanceAndCat2(TourItemResponse response);
}
