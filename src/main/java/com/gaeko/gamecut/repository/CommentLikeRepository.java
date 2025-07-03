// 2025-07-03 생성됨
package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    
    // 댓글 좋아요 존재 여부 확인
    boolean existsByUserUserNoAndCommentCommentNo(Integer userNo, Integer commentNo);
    
    // 댓글 좋아요 삭제
    @Modifying
    @Query("DELETE FROM CommentLike cl WHERE cl.user.userNo = :userNo AND cl.comment.commentNo = :commentNo")
    void deleteByUserNoAndCommentNo(@Param("userNo") Integer userNo, @Param("commentNo") Integer commentNo);
}
