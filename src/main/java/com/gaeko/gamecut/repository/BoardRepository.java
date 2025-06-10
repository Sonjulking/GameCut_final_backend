package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findBoardByBoardNo(Integer boardNo);

    //board type이 3인 게시물을, 삭제일이 없는 게시물을 랜덤으로 불러줌
    @Query("SELECT b FROM Board b WHERE b.boardType.boardTypeNo = 3 AND b.boardDeleteDate IS NULL ORDER BY function('DBMS_RANDOM.VALUE')")
    List<Board> findRandom5BoardType3NotDeleted(Pageable pageable);

}
