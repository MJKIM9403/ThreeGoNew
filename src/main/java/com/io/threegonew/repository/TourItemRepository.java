package com.io.threegonew.repository;

import com.io.threegonew.domain.TourItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourItemRepository extends JpaRepository<TourItem, String> {
}
