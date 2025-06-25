package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.Tag;
import com.gaeko.gamecut.entity.TagByVideo;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.repository.TagByVideoRepository;
import com.gaeko.gamecut.repository.TagRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagByVideoService {
    private final TagByVideoRepository tagByVideoRepository;
    private final TagRepository tagRepository;
    private final VideoRepository videoRepository;

    public void insert(String inputTagName, Integer videoId) {
        TagByVideo tagByVideo = new TagByVideo();

        Optional<Tag> inputTag = tagRepository.findById(inputTagName);
        Optional<Video> video = videoRepository.findById(videoId);
        tagByVideoRepository.deleteTagByVideo(video.get());


        tagByVideo.setTag(inputTag.get());
        tagByVideo.setVideo(video.get());


        tagByVideoRepository.save(tagByVideo);
    }


    /**
     * 전체 태그 관계 삭제
     **/
    public void deleteByVideo(Integer videoId) {
        Video video = videoRepository.findById(videoId)
                                     .orElseThrow(() -> new IllegalArgumentException("Video not found: " + videoId));
        tagByVideoRepository.deleteTagByVideo(video);
        video.getTagByVideoList().clear();
    }

    /**
     * 단순 삽입만 수행 (삭제는 컨트롤러에서 한 번만)
     **/
    public void insertOnly(String inputTagName, Integer videoId) {
        Tag tag = tagRepository.findById(inputTagName)
                               .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + inputTagName));
        Video video = videoRepository.findById(videoId)
                                     .orElseThrow(() -> new IllegalArgumentException("Video not found: " + videoId));

        TagByVideo tbv = TagByVideo.builder()
                                   .tag(tag)
                                   .video(video)
                                   .build();
        tagByVideoRepository.save(tbv);
    }
}
