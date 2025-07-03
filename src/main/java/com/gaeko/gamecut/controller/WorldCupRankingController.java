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

    @GetMapping("/ranking")
    public List<VideoRankingDTO> getAllRanking() {
        return rankingService.getRanking();
    }
}
