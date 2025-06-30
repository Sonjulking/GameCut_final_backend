package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.MadmovieWorldCupResultDTO;
import com.gaeko.gamecut.service.WorldCupResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/worldcup")
@RequiredArgsConstructor
public class WorldCupResultController {

    private final WorldCupResultService resultService;

    @PostMapping("/result")
    public ResponseEntity<MadmovieWorldCupResultDTO> saveResult(
        @RequestParam("userNo") Integer userNo,
        @RequestParam("videoNo") Integer videoNo
    ) {
        MadmovieWorldCupResultDTO dto = resultService.saveChampion(userNo, videoNo);
        return ResponseEntity.ok(dto);
    }
}
