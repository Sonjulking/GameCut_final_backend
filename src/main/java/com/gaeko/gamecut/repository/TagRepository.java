package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    // LAZY 로딩된 file을 JOIN FETCH로 즉시 로딩
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.file")
    List<Tag> findAllWithFile();

    // tagName을 기준으로 fileUrl 조회
    @Query("SELECT f.fileUrl FROM Tag t JOIN t.file f WHERE t.tagName = :tagName")
    String findFileUrlByTagName(@Param("tagName") String tagName);
}
