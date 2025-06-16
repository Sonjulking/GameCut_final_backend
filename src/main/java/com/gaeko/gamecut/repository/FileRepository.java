package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    File findFileByAttachNo(Integer attachNo);


    File findFileByFileUrl(String fileUrl);
}
