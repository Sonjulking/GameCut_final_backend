package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.VideoRankingDTO;
import com.gaeko.gamecut.service.WorldCupRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worldcup")
@RequiredArgsConstructor
public class WorldCupRankingController {

    private final WorldCupRankingService rankingService;

    // // /api/worldcup/ranking?worldCupNo=7
    // @GetMapping("/ranking")
    // public List<VideoRankingDTO> getRanking(@RequestParam Integer worldCupNo) {
    //     return rankingService.getRanking(worldCupNo);
    // }

    @GetMapping("/ranking")
    public List<VideoRankingDTO> getAllRanking() {
        return rankingService.getRanking();
    }
}
