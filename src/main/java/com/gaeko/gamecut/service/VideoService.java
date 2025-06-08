package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.mapper.VideoMapper;
import com.gaeko.gamecut.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    //비디오 저장
    public VideoDTO save(VideoDTO dto) {
        Video video = videoMapper.toEntity(dto);
        video = videoRepository.save(video);
        return videoMapper.toDTO(video);
    }

}
