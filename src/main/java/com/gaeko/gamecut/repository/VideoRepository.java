package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findVideoByVideoNo(Integer videoNo);

    @Transactional
    @Modifying
    @Query("DELETE FROM Video v WHERE v.board.boardNo = :boardNo")
    void deleteByBoardNo(@Param("boardNo") Integer boardNo);


}
