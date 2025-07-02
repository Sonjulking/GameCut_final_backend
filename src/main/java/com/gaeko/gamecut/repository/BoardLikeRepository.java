package com.gaeko.gamecut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.gaeko.gamecut.entity.BoardLike;
import com.gaeko.gamecut.entity.BoardLikeId;

import jakarta.transaction.Transactional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardLikeId> {

    // 좋아요 여부 (게시글번호, 유저번호로)
    @Query("SELECT CASE WHEN COUNT(bl) > 0 THEN true ELSE false END FROM BoardLike bl WHERE bl.user.userNo = :userNo AND bl.board.boardNo = :boardNo")
    boolean existsByUserNoAndBoardNo(@Param("userNo") Integer userNo, @Param("boardNo") Integer boardNo);

    // 좋아요 취소
    @Modifying
    @Transactional
    @Query("DELETE FROM BoardLike bl WHERE bl.user.userNo = :userNo AND bl.board.boardNo = :boardNo")
    int deleteByUserNoAndBoardNo(@Param("userNo") Integer userNo, @Param("boardNo") Integer boardNo);
}
