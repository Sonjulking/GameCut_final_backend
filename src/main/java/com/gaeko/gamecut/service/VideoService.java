package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.mapper.VideoMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;

    //비디오 저장

    @Transactional
    public VideoDTO save(VideoDTO videoDTO) {
        Video video = videoMapper.toEntity(videoDTO);

        File file = fileRepository.findFileByAttachNo(videoDTO.getAttachFile().getAttachNo());
        Board board = boardRepository.findBoardByBoardNo(videoDTO.getBoardNo());
        video.setAttachFile(file);
        video.setBoard(board);
        video = videoRepository.save(video);
        return videoMapper.toDTO(video);
    }

    @Transactional
    public VideoDTO save(Integer boardNo, Integer attachNo) {
        // 이미 해당 board에 video가 연결되어 있으면 insert 막기
        Video video = new Video();
        File file = fileRepository.findFileByAttachNo(attachNo);
        Board board = boardRepository.findBoardByBoardNo(boardNo);
        video.setAttachFile(file);
        video.setBoard(board);
        video = videoRepository.save(video);
        return videoMapper.toDTO(video);
    }

    public VideoDTO findByVideoNo(Integer videoNo) {

        Video video = videoRepository.findVideoByVideoNo(videoNo);
        return videoMapper.toDTO(video);
    }

}
