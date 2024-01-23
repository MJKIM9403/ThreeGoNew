package com.io.threegonew.repository;

import com.io.threegonew.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsById(String id);

}
