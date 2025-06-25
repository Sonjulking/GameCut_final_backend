package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.Tag;
import com.gaeko.gamecut.entity.TagByVideo;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.repository.TagByVideoRepository;
import com.gaeko.gamecut.repository.TagRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagByVideoService {
    private final TagByVideoRepository tagByVideoRepository;
    private final TagRepository tagRepository;
    private final VideoRepository videoRepository;

    public void insert(String tagName, Integer videoId) {
        TagByVideo tagByVideo = new TagByVideo();
        Optional<Tag> tag = tagRepository.findById(tagName);
        Optional<Video> video = videoRepository.findById(videoId);
        tagByVideo.setTag(tag.get());
        tagByVideo.setVideo(video.get());

        tagByVideoRepository.save(tagByVideo);
    }
}
