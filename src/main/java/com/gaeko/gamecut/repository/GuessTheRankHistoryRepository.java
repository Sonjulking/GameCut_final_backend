package com.gaeko.gamecut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gaeko.gamecut.entity.GuessTheRankHistory;
import java.util.List;

@Repository
public interface GuessTheRankHistoryRepository extends JpaRepository<GuessTheRankHistory, Integer> {
    // 2025-07-09 수정됨 - 사용자별 게임 기록 조회 (최신 순)
    List<GuessTheRankHistory> findByUser_UserNoOrderBySolveDateDesc(Integer userNo);
}