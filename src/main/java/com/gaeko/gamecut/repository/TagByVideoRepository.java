package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.TagByVideo;
import com.gaeko.gamecut.entity.TagByVideoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagByVideoRepository extends JpaRepository<TagByVideo, TagByVideoId> {

}
