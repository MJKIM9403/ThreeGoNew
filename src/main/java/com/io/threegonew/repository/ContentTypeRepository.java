package com.io.threegonew.repository;

import com.io.threegonew.domain.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentTypeRepository extends JpaRepository<ContentType, String> {
    boolean existsById(String contentTypeId);
}
