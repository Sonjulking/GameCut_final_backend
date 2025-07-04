package com.gaeko.gamecut.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.entity.UserItem;
import com.gaeko.gamecut.entity.UserItemId;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, UserItemId> {

    // 특정 유저가 구매한 모든 아이템 조회
    List<UserItem> findByUser(User user);
}
