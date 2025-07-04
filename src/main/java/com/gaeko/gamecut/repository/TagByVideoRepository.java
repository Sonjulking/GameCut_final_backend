package com.gaeko.gamecut.repository;

import com.gaeko.gamecut.entity.Tag;
import com.gaeko.gamecut.entity.TagByVideo;
import com.gaeko.gamecut.entity.TagByVideoId;
import com.gaeko.gamecut.entity.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagByVideoRepository extends JpaRepository<TagByVideo, TagByVideoId> {

    List<TagByVideo> findTagByVideo(Video video);

    @Transactional
    @Modifying
    void deleteByTagAndVideo(Tag tag, Video video);

    @Transactional
    @Modifying
    void deleteTagByVideo(Video video);

    @Transactional
    @Modifying
    @Query("DELETE FROM TagByVideo t WHERE t.video.videoNo = :videoNo")
    void deleteTagByVideo(@Param("videoNo") Integer videoNo);
}
