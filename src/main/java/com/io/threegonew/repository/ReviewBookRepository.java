package com.io.threegonew.repository;

import com.io.threegonew.domain.ReviewBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewBookRepository extends JpaRepository<ReviewBook, Long> {
}
