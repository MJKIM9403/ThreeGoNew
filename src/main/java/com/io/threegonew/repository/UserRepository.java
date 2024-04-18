package com.io.threegonew.repository;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.UserWithFollowStateInterface;
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


    @Query(value = "SELECT u.u_id AS id, " +
            "u.u_name AS name, " +
            "u.u_sfile AS profileImg, " +
            "u.u_about AS about, " +
            "CASE WHEN f_out.id IS NOT NULL THEN 1 ELSE 0 END AS followedState, " +
            "CASE WHEN f_in.id IS NOT NULL THEN 1 ELSE 0 END AS followingState " +
            "FROM users u " +
            "LEFT JOIN follows f_out ON u.u_id = f_out.to_user_id AND f_out.from_user_id = :loginUser " +
            "LEFT JOIN follows f_in ON u.u_id = f_in.from_user_id AND f_in.to_user_id = :loginUser " +
            "WHERE u.u_id LIKE :containKeyword " +
            "ORDER BY " +
            "CASE " +
                "WHEN u.u_id LIKE :startKeyword " +
                    "THEN 1 " +
                    "ELSE 2 " +
            "END, " +
            "u.u_id ASC ",
            countQuery = "SELECT count(u.u_id) FROM users u WHERE u.u_id LIKE :containKeyword ",
            nativeQuery = true)
    Page<UserWithFollowStateInterface> findUsersByStartOrContainUserId(Pageable pageable, @Param("startKeyword")String startKeyword, @Param("containKeyword")String containKeyword, @Param("loginUser")String loginUser);

    @Query(value = "SELECT u.u_id AS id, " +
            "u.u_name AS name, " +
            "u.u_sfile AS profileImg, " +
            "u.u_about AS about, " +
            "CASE WHEN f_out.id IS NOT NULL THEN 1 ELSE 0 END AS followedState, " +
            "CASE WHEN f_in.id IS NOT NULL THEN 1 ELSE 0 END AS followingState " +
            "FROM users u " +
            "LEFT JOIN follows f_out ON u.u_id = f_out.to_user_id AND f_out.from_user_id = :loginUser " +
            "LEFT JOIN follows f_in ON u.u_id = f_in.from_user_id AND f_in.to_user_id = :loginUser " +
            "WHERE u.u_name LIKE :containKeyword " +
            "ORDER BY " +
            "CASE " +
            "WHEN u.u_name LIKE :startKeyword " +
            "THEN 1 " +
            "ELSE 2 " +
            "END, " +
            "u.u_name ASC ",
            countQuery = "SELECT count(u.u_id) FROM users u WHERE u.u_name LIKE :containKeyword ",
            nativeQuery = true)
    Page<UserWithFollowStateInterface> findUsersByStartOrContainUserName(Pageable pageable, @Param("startKeyword")String startKeyword, @Param("containKeyword")String containKeyword, @Param("loginUser")String loginUser);
}
