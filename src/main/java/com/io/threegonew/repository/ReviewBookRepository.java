package com.io.threegonew.repository;

import com.io.threegonew.domain.ReviewBook;
import com.io.threegonew.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewBookRepository extends JpaRepository<ReviewBook, Long>, ReviewBookRepositoryCustom {

    List<ReviewBook> findByUser(User user);
}
