package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findVideoByVideoNo(Integer videoNo);
}
