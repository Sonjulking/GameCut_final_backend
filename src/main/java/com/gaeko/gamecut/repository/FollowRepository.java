package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Follow;
import com.gaeko.gamecut.entity.FollowId;
import com.gaeko.gamecut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    boolean existsByFolloweeAndFollower(User followee, User follower);
    void deleteByFolloweeAndFollower(User followee, User follower);
    Optional<Follow> findByFolloweeAndFollower(User followee, User follower);
}