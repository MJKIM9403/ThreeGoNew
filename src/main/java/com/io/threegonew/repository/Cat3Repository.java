package com.io.threegonew.repository;

import com.io.threegonew.domain.Cat3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Cat3Repository extends JpaRepository<Cat3, String> {
    boolean existsById(String cat3);
}
