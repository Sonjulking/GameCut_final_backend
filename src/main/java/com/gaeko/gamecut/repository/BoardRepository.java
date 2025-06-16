package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findBoardByBoardNo(Integer boardNo);

    @Query(value = "SELECT * FROM board WHERE board_type_no = 3 AND board_delete_date IS NULL ORDER BY DBMS_RANDOM.VALUE FETCH FIRST 5 ROWS ONLY", nativeQuery = true)
    List<Board> findRandom5BoardType3NotDeleted();

    @Query("SELECT b FROM Board b WHERE b.boardType.boardTypeNo = 3 AND b.boardDeleteDate IS NULL ORDER BY function('DBMS_RANDOM.VALUE')")
    List<Board> findRandomOneBoard(Pageable pageable);

    @Query(value = "SELECT * FROM BOARD b " +
            "WHERE b.BOARD_TYPE_NO = 3 " +
            "AND b.BOARD_DELETE_DATE IS NULL " +
            "AND b.BOARD_NO NOT IN (:excludeIds) " +
            "ORDER BY DBMS_RANDOM.VALUE", nativeQuery = true)
    List<Board> findRandomOneBoardExclude(@Param("excludeIds") List<Long> excludeIds, Pageable pageable);


}
