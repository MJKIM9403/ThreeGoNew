package com.io.threegonew.repository;

import com.io.threegonew.domain.Bookmark;
import com.io.threegonew.domain.TourItem;
import com.io.threegonew.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Long countBookmarkByTourItem(TourItem tourItem);

    boolean existsBookmarkByTourItemAndUser(TourItem tourItem, User user);

    Optional<Bookmark> findByTourItemAndUser(TourItem tourItem, User user);
}
