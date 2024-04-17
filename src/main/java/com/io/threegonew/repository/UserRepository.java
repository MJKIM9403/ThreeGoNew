package com.io.threegonew.repository;

import com.io.threegonew.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    //유무 확인
    boolean existsById(String id);
    // Email 존재 여부 확인
    boolean existsByEmail(String email);
    Optional<User> findById(String id);
    List<User> findAll();
    Optional<User> findByEmail(String email);

    void deleteById(String id);


    @Query(value = "SELECT u.* " +
            "FROM users u" +
            "WHERE u.U_ID LIKE :containKeyword " +
            "ORDER BY " +
            "CASE " +
                "WHEN u.U_ID LIKE :startKeyword " +
                    "THEN 1 " +
                    "ELSE 2 " +
            "END, " +
            "U_ID ASC ",
            countQuery = "SELECT count(u.U_ID) FROM users u WHERE u.U_ID LIKE :containKeyword ",
            nativeQuery = true)
    Page<User> findUsersStartOrContain(Pageable pageable, @Param("startKeyword")String startKeyword, @Param("containKeyword")String containKeyword);

}
