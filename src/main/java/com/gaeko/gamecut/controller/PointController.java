package com.gaeko.gamecut.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaeko.gamecut.service.PointService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/ranking/monthly")
    public List<Map<String, Object>> getMonthlyRanking() {
        return pointService.getMonthlyRanking();
    }
    
    // 누적 랭킹
    @GetMapping("/ranking/total")
    public List<Map<String, Object>> getTotalRanking() {
        return pointService.getTotalRanking();
    }
}
