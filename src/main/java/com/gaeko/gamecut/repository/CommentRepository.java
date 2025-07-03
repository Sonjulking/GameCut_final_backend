// 2025-07-03 생성됨
// 2025-07-03 생성됨
package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.user.userNo = :userNo AND c.commentDeleteDate IS NULL ORDER BY c.commentCreateDate DESC")
    List<Comment> findByUserNo(@Param("userNo") Integer userNo);

    @Modifying
    @Query(value = "UPDATE COMMENT_TB SET COMMENT_DELETE_DATE = SYSDATE WHERE COMMENT_NO = :commentNo", nativeQuery = true)
    void deleteByCommentNo(@Param("commentNo") Integer commentNo);

    @Modifying
    @Query(value = "UPDATE COMMENT_TB SET COMMENT_CONTENT = :#{#commentDTO.commentContent} WHERE COMMENT_NO = :commentNo", nativeQuery = true)
    void updateComment(Integer commentNo, CommentDTO commentDTO);

    // 특정 게시글의 상위 5개 댓글 조회 (최신순)
    @Query(value = "SELECT * FROM (" +
            "    SELECT c.* FROM comment_tb c " +
            "    WHERE c.board_no = :boardNo " +
            "    AND c.comment_delete_date IS NULL " +
            "    ORDER BY c.comment_create_date DESC" +
            ") WHERE ROWNUM <= 5",
            nativeQuery = true)
    List<Comment> findTop5CommentsByBoardNo(@Param("boardNo") Integer boardNo);

    // 여러 게시글의 상위 5개 댓글을 배치로 조회 (N+1 문제 해결)
    @Query(value = "SELECT * FROM (" +
            "    SELECT c.*, " +
            "           ROW_NUMBER() OVER (PARTITION BY c.board_no ORDER BY c.comment_create_date DESC) as rn " +
            "    FROM comment_tb c " +
            "    WHERE c.board_no IN (:boardNos) " +
            "    AND c.comment_delete_date IS NULL" +
            ") WHERE rn <= 5 " +
            "ORDER BY board_no, comment_create_date DESC",
            nativeQuery = true)
    List<Comment> findTop5CommentsByBoardNos(@Param("boardNos") List<Integer> boardNos);

    // 특정 게시글의 댓글 페이징 조회 (최신순)
    @Query("SELECT c FROM Comment c " +
            "WHERE c.board.boardNo = :boardNo " +
            "AND c.commentDeleteDate IS NULL " +
            "ORDER BY c.commentCreateDate DESC")
    List<Comment> findCommentsByBoardNo(@Param("boardNo") Integer boardNo);

    // 특정 게시글의 댓글 총 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c " +
            "WHERE c.board.boardNo = :boardNo " +
            "AND c.commentDeleteDate IS NULL")
    Long countCommentsByBoardNo(@Param("boardNo") Integer boardNo);

}