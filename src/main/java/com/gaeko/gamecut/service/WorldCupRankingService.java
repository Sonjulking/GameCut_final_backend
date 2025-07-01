package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.VideoRankingDTO;
import com.gaeko.gamecut.entity.Board;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.repository.BoardRepository;
import com.gaeko.gamecut.repository.MadmovieWorldCupResultRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorldCupRankingService {

    private final MadmovieWorldCupResultRepository resultRepo;
    private final VideoRepository               videoRepo;
    private final BoardRepository               boardRepo;

    private static final int VIDEO_BOARD_TYPE = 3;

    public List<VideoRankingDTO> getRanking() {
        long totalRuns = resultRepo.count();

        // “현재 영상 게시판” 에 남아 있는 videoNo 집합
        Set<Integer> activeVideoNos = boardRepo
            .findByBoardType_BoardTypeNo(VIDEO_BOARD_TYPE)
            .stream()
            .map(Board::getVideo)
            .filter(Objects::nonNull)
            .map(Video::getVideoNo)
            .collect(Collectors.toSet());

        List<Object[]> rows = resultRepo.countWinsAll();
        List<VideoRankingDTO> dtos = new ArrayList<>();

        for (Object[] row : rows) {
            Integer videoNo = (Integer) row[0];
            Long    winCount = (Long)    row[1];

            if (!activeVideoNos.contains(videoNo)) {
                continue; // 게시판에서 내려간 영상은 스킵
            }

            Video v = videoRepo.findById(videoNo)
                       .orElseThrow(() -> new IllegalArgumentException("Invalid videoNo: " + videoNo));

            dtos.add(VideoRankingDTO.builder()
                .videoNo       (videoNo)
                .winCount      (winCount)
                .totalRuns     (totalRuns)
                .winRate       ( totalRuns>0 ? winCount/(double)totalRuns : 0.0 )
                .videoRealPath (v.getAttachFile().getRealPath())
                .build());
        }

        return dtos;
    }
}
