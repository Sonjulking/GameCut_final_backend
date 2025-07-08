package com.gaeko.gamecut.repository;

import java.util.List;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.gaeko.gamecut.entity.GuessTheRank;


@Repository
public interface GuessTheRankRepository extends JpaRepository<GuessTheRank, Integer> {
  @Query("SELECT g FROM GuessTheRank g ORDER BY function('dbms_random.value')")
  List<GuessTheRank> findRandomOne(Pageable pageable);
  
  // 2025년 7월 8일 수정됨 - 게임 종류별 조회
  List<GuessTheRank> findByGameType(String gameType);
}
