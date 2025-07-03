package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Block;
import com.gaeko.gamecut.entity.BlockId;
import com.gaeko.gamecut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, BlockId> {
    boolean existsByBlockerAndBlocked(User blocker, User blocked);
    void deleteByBlockerAndBlocked(User blocker, User blocked);

}

