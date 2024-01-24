package com.io.threegonew.repository;

import com.io.threegonew.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    //유무 확인
    boolean existsById(String id);
    // Email 존재 여부 확인
    boolean existsByEmail(String email);

    Optional<User> findById(String id);

}
