package com.gaeko.gamecut.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gaeko.gamecut.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserNo(Integer userNo);
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserIdAndEmail(String userId, String email);
    Optional<User> findByUserNickname(String userNickname);

    @Query("SELECT u.userNo FROM User u WHERE u.userId = :userId")
    Integer findUserNoByUserId(@Param("userId") String userId);

    @Modifying  // 이게 핵심!
    @Transactional  // 트랜잭션 추가
    @Query(value = "UPDATE USER_TB SET USER_DELETE_DATE = SYSTIMESTAMP WHERE USER_ID = :userId", nativeQuery = true)
    void userDelete(@Param("userId") String userId);
    
    //문자전송위한 함수
    Optional<User> findByUserIdAndPhone(String userId, String phone);
    
}
