package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserNo(Integer userNo);
}
