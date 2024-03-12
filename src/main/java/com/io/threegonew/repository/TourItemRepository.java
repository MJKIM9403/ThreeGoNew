package com.io.threegonew.repository;

import com.io.threegonew.dto.MoreTourItemInterface;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.dto.TourItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TourItemRepository extends JpaRepository<TourItem, String>, JpaSpecificationExecutor<TourItem>, TourItemRepositoryCustom {
    Page<TourItem> findAll(Specification spec, Pageable pageable);

    Optional<TourItem> findByContentid(String contentid);

    // 현재 조회중인 페이지의 관광지와 같은 소분류 관광지를 가까운 거리순으로 조회(현재 페이지 관광지 제외)
    @Query(value = "SELECT t.contentid, t.title, t.firstimage, t.mapx, t.mapy, " +
            "truncate(( 6371 * acos( cos( radians(:#{#response.mapy}) ) * cos( radians( t.mapy ) ) " +
            "* cos( radians( :#{#response.mapx} ) - radians( t.mapx ) ) " +
            "+ sin( radians( :#{#response.mapy} ) ) * sin( radians( t.mapy ) ) ) ), 2) AS distance " +
            "FROM touritem t " +
            "where cat3 = :#{#response.cat3} " +
            "and contentid not in ( :#{#response.contentid} ) " +
            "HAVING distance < 3 " +
            "ORDER BY distance " +
            "LIMIT 0 , 10", nativeQuery = true)
    List<MoreTourItemInterface> findSubItemByCat3OrderByDistance(TourItemResponse response);


}
