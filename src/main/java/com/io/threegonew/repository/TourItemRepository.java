package com.io.threegonew.repository;

import com.io.threegonew.domain.TourItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TourItemRepository extends JpaRepository<TourItem, String>, JpaSpecificationExecutor<TourItem> {

}
