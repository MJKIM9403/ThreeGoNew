package com.io.threegonew.repository;

import com.io.threegonew.domain.Area;
import com.io.threegonew.domain.TourItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Integer> {
    boolean existsById(Integer areaCode);
}
