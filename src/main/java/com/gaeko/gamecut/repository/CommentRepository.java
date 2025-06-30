package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.CommentDTO;
import com.gaeko.gamecut.entity.Comment;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

	
}
