package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.VideoRankingDTO;
import com.gaeko.gamecut.entity.Video;
import com.gaeko.gamecut.repository.MadmovieWorldCupResultRepository;
import com.gaeko.gamecut.repository.VideoRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorldCupRankingService {

    private final MadmovieWorldCupResultRepository resultRepo;
    private final VideoRepository videoRepo;

    // worldCupNo 별 랭킹 가져오기
    public List<VideoRankingDTO> getRanking(Integer worldCupNo) {
        long totalRuns = resultRepo.countByWorldCupNo(worldCupNo);
        List<Object[]> rows = resultRepo.countWinsByWorldCupNo(worldCupNo);

        List<VideoRankingDTO> dtos = new ArrayList<>();
        for (Object[] row : rows) {
            Integer videoNo = (Integer) row[0];
            Long winCount   = (Long)    row[1];

            Video v = videoRepo.findById(videoNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid videoNo: " + videoNo));

            dtos.add(VideoRankingDTO.builder()
                .videoNo(videoNo)
                .videoRealPath(v.getAttachFile().getRealPath())
                .winCount(winCount)
                .totalRuns(totalRuns)
                .winRate(totalRuns > 0 ? winCount / (double) totalRuns : 0.0)
                .build());
        }
        return dtos;
    }
}
