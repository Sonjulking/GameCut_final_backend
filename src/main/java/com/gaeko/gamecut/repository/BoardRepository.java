package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    List<Board> findRandomOneBoardExclude(
            @Param("excludeIds") List<Long> excludeIds,
            Pageable pageable
    );



    Page<Board> findAllByBoardType(Pageable pageable, BoardType boardType);
    
    List<Board> findByBoardType_BoardTypeNo(int boardTypeNo);

    // 🔥 삭제되지 않은 전체 게시글 (페이징 자동 적용)
    Page<Board> findByBoardDeleteDateIsNull(Pageable pageable);
    
    // 🔥 삭제되지 않은 특정 타입 게시글 (페이징 자동 적용)  
    Page<Board> findByBoardDeleteDateIsNullAndBoardType(Pageable pageable, BoardType boardType);

    @Modifying
    @Query("UPDATE Board b SET b.boardDeleteDate = SYSDATE WHERE b.boardNo = :boardNo")
    void deleteByBoardNo(@Param("boardNo") Integer boardNo);

    // 검색어 기반 게시물 조회 
    @Query(
      "SELECT b FROM Board b " +
      " WHERE b.boardDeleteDate IS NULL" +
      "   AND (:boardTypeNo IS NULL OR b.boardType.boardTypeNo = :boardTypeNo)" +
      "   AND ( :keyword IS NULL" +
      "      OR ( b.boardTitle   LIKE CONCAT('%',:keyword,'%')" +
      "        OR b.boardContent LIKE CONCAT('%',:keyword,'%')" +
      "        OR b.user.userNickname LIKE CONCAT('%',:keyword,'%')" +
      "      )" +
      "   )" +
      " ORDER BY b.boardNo DESC"
    )
    Page<Board> search(
      @Param("boardTypeNo") Integer boardTypeNo,
      @Param("keyword")    String  keyword,
      Pageable             pageable
    );
}
