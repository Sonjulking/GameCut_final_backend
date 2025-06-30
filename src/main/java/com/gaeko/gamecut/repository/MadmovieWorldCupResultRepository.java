package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.MadmovieWorldCupResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MadmovieWorldCupResultRepository
    extends JpaRepository<MadmovieWorldCupResult, Integer> {

    // 월드컵 번호로 필터링한 승리 집계
    @Query("""
      SELECT r.video.videoNo, COUNT(r)
      FROM MadmovieWorldCupResult r
      WHERE r.worldCupNo = :worldCupNo
      GROUP BY r.video.videoNo
      ORDER BY COUNT(r) DESC
    """)
    List<Object[]> countWinsByWorldCupNo(@Param("worldCupNo") Integer worldCupNo);

    // same 월드컵No 의 총 게임 수
    long countByWorldCupNo(Integer worldCupNo);
}
