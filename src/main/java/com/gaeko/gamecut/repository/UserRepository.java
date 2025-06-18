package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.UserDTO;


import com.gaeko.gamecut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserNo(Integer userNo);
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserIdAndEmail(String userId, String email);
    Optional<User> findByUserNickname(String userNickname);

    @Query("SELECT u.userNo FROM User u WHERE u.userId = :userId")
    Integer findUserNoByUserId(@Param("userId") String userId);
}
