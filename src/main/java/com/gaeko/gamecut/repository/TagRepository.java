package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    // LAZY 로딩된 file을 JOIN FETCH로 즉시 로딩
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.file")
    List<Tag> findAllWithFile();
}
