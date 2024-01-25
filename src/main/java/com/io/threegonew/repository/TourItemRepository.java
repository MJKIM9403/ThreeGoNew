package com.io.threegonew.repository;

import com.io.threegonew.domain.TourItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TourItemRepository extends JpaRepository<TourItem, String>, JpaSpecificationExecutor<TourItem> {
    Page<TourItem> findAll(Specification spec, Pageable pageable);
}
