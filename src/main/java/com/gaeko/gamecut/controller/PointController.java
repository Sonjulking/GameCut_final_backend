package com.gaeko.gamecut.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaeko.gamecut.dto.PointHistoryDTO;
import com.gaeko.gamecut.service.PointService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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
    
    @GetMapping("/point/my")
    public List<PointHistoryDTO> getMyPointHistory(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        return pointService.getPointHistoryByUserId(userId);
    }

}
