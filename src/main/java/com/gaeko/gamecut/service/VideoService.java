package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.File;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.mapper.VideoMapper;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
        // 기존 Video 삭제
        videoRepository.deleteByBoardNo(boardNo);

        // 새 객체 생성 (주의: 기존 객체를 재사용하면 안 됨!)
        File file = fileRepository.findFileByAttachNo(attachNo);
        Board board = boardRepository.findBoardByBoardNo(boardNo);

        Video newVideo = new Video(); // 여기서 꼭 새로 생성해야 함
        newVideo.setAttachFile(file);
        newVideo.setBoard(board);
        Video savedVideo = videoRepository.save(newVideo); // save 전에 병합되지 않은 객체만 넘겨야 함

        return videoMapper.toDTO(savedVideo);
    }


    public VideoDTO findByVideoNo(Integer videoNo) {

        Video video = videoRepository.findVideoByVideoNo(videoNo);
        return videoMapper.toDTO(video);
    }

    /** 모든 영상 조회 */
    @Transactional(readOnly = true)
    public List<VideoDTO> findAllVideos() {
        return videoRepository.findAll().stream().map(entity -> {
            VideoDTO dto = videoMapper.toDTO(entity);
            String rp = entity.getAttachFile().getRealPath();
            // "/upload/" 이후만 URL로 사용
            int idx = rp.indexOf("/upload/");
            dto.setUrl(idx >= 0 ? rp.substring(idx) : "");
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
public VideoDTO saveGameVideo(Integer attachNo) {
    File file = fileRepository.findFileByAttachNo(attachNo);
    if (file == null) {
        throw new IllegalArgumentException("파일 첨부내역이 없습니다: " + attachNo);
    }

    Video video = Video.builder()
        .attachFile(file)
        // 2025-07-08 수정됨 - tempBoard 제거, board는 null로 설정 (필요시 별도로 설정)
        .build();

    Video saved = videoRepository.save(video);

    return videoMapper.toDTO(saved);
}

}
