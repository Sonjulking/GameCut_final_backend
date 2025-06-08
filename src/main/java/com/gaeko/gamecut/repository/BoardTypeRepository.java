package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardTypeRepository extends JpaRepository<BoardType, Integer> {
    BoardType findBoardTypeByBoardTypeNo(int boardTypeNo);
}
