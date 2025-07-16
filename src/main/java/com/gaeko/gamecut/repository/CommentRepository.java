// 2025-07-03 생성됨
// 2025-07-03 생성됨
package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "UPDATE comment_tb SET comment_delete_date = NOW() WHERE comment_no = :commentNo", nativeQuery = true)
    void deleteByCommentNo(@Param("commentNo") Integer commentNo);

    @Modifying
    @Query(value = "UPDATE comment_tb SET comment_content = :#{#commentDTO.commentContent} WHERE comment_no = :commentNo", nativeQuery = true)
    void updateComment(Integer commentNo, CommentDTO commentDTO);

    // 특정 게시글의 상위 5개 댓글 조회 (최신순)
    @Query(value = "SELECT * FROM (" +
            "    SELECT c.* FROM comment_tb c " +
            "    WHERE c.board_no = :boardNo " +
            "    AND c.comment_delete_date IS NULL " +
            "    ORDER BY c.comment_create_date DESC" +
            ") sub LIMIT 5",
            nativeQuery = true)
    List<Comment> findTop5CommentsByBoardNo(@Param("boardNo") Integer boardNo);

    // 좋아요 ↓, 작성일 ↓ 로 정렬 후 상위 5개만
    @Query("""
            SELECT c FROM Comment c
            WHERE c.board.boardNo = :boardNo
              AND c.commentDeleteDate IS NULL
            ORDER BY c.commentLike DESC, c.commentCreateDate DESC
            """)
    Page<Comment> findTop5Comment(@Param("boardNo") Integer boardNo, Pageable pageable);


    @Query(value = """
    SELECT * FROM (
        SELECT  c.*,
                ROW_NUMBER() OVER (
                    PARTITION BY c.board_no
                    ORDER BY c.comment_like DESC,
                             c.comment_create_date DESC
                ) AS rn
        FROM comment_tb c
        WHERE c.board_no IN (:boardNos)
          AND c.comment_delete_date IS NULL
    ) sub
    WHERE rn <= 5
    ORDER BY board_no,
             comment_like DESC,
             comment_create_date DESC
    """, nativeQuery = true)
    List<Comment> findTop5CommentsByBoardNos(@Param("boardNos") List<Integer> boardNos);

    // 특정 게시글의 댓글 페이징 조회 (최신순)
    @Query("SELECT c FROM Comment c " +
            "WHERE c.board.boardNo = :boardNo " +
            "ORDER BY c.commentCreateDate DESC")
    List<Comment> findCommentsByBoardNo(@Param("boardNo") Integer boardNo);

    // 특정 게시글의 댓글 총 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c " +
            "WHERE c.board.boardNo = :boardNo ")
    Long countCommentsByBoardNo(@Param("boardNo") Integer boardNo);

    // 2025-07-14 수정됨 - 삭제된 댓글도 포함하여 조회하는 새로운 메소드 (pages용)
    @Query("SELECT c FROM Comment c " +
            "WHERE c.board.boardNo = :boardNo " +
            "ORDER BY c.commentLike DESC, c.commentCreateDate DESC")
    List<Comment> findAllCommentsByBoardNoIncludingDeleted(@Param("boardNo") Integer boardNo);

    // 2025-07-14 수정됨 - 삭제된 댓글도 포함한 총 개수 조회 (pages용)
    @Query("SELECT COUNT(c) FROM Comment c " +
            "WHERE c.board.boardNo = :boardNo")
    Long countAllCommentsByBoardNo(@Param("boardNo") Integer boardNo);

    // 2025-07-14 수정됨 - 메인화면용: 삭제된 댓글 포함, 좋아요순 정렬 된 상위 5개 댓글 조회
    @Query("""
            SELECT c FROM Comment c
            WHERE c.board.boardNo = :boardNo
            ORDER BY c.commentLike DESC, c.commentCreateDate DESC
            """)
    Page<Comment> findTop5CommentIncludingDeleted(@Param("boardNo") Integer boardNo, Pageable pageable);

    // 2025-07-14 수정됨 - 메인화면용: 삭제된 댓글 포함, 좋아요순 정렬
    @Query(value = """
    SELECT * FROM (
        SELECT  c.*,
                ROW_NUMBER() OVER (
                    PARTITION BY c.board_no
                    ORDER BY c.comment_like DESC,
                             c.comment_create_date DESC
                ) AS rn
        FROM comment_tb c
        WHERE c.board_no IN (:boardNos)
    ) sub
    WHERE rn <= 5
    ORDER BY board_no,
             comment_like DESC,
             comment_create_date DESC
    """, nativeQuery = true)
    List<Comment> findTop5CommentsByBoardNosIncludingDeleted(@Param("boardNos") List<Integer> boardNos);

}