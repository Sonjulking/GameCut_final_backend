package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.entity.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    File findFileByAttachNo(Integer attachNo);


    File findFileByFileUrl(String fileUrl);

    @Query("""
                SELECT f.realPath FROM File f 
                WHERE f.attachNo NOT IN (
                    SELECT p.attachFile.attachNo FROM Photo p WHERE p.attachFile IS NOT NULL
                    UNION
                    SELECT v.attachFile.attachNo FROM Video v WHERE v.attachFile IS NOT NULL
                )
                AND f.uploadTime <= :weekAgo
            """)
    List<String> findRealPathNotUsedInPhotoAndVideo(@Param("weekAgo") LocalDateTime weekAgo);

    @Query("""
    SELECT f.attachNo FROM File f 
    WHERE f.attachNo NOT IN (
        SELECT p.attachFile.attachNo FROM Photo p WHERE p.attachFile IS NOT NULL
        UNION
        SELECT v.attachFile.attachNo FROM Video v WHERE v.attachFile IS NOT NULL
    )
    AND f.uploadTime <= :weekAgo
""")
    List<Integer> findAttachNoUsedInPhotoAndVideo(@Param("weekAgo") LocalDateTime weekAgo);


    @Transactional
    void deleteByAttachNo(Integer attachNo);
}
