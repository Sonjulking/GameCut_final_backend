package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.service.GuessTheRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final GuessTheRankService rankService;

    /** POST /api/videos/{videoNo}/tier?tier=골드 */
    @PostMapping("/{videoNo}/tier")
    public ResponseEntity<?> setTier(
        @PathVariable Integer videoNo,
        @RequestParam String tier
    ) {
        rankService.saveTier(videoNo, tier);
        return ResponseEntity.ok().build();
    }
}
