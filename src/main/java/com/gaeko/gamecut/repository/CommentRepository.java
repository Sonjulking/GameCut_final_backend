package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Comment;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("SELECT c FROM Comment c WHERE c.user.userNo = :userNo AND c.commentDeleteDate IS NULL ORDER BY c.commentCreateDate DESC")
	List<Comment> findByUserNo(@Param("userNo") Integer userNo);

	
}
