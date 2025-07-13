package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    // POST /block - 차단 또는 차단해제
    @PostMapping
    public Map<String, Object> toggleBlock(@RequestBody Map<String, Integer> body) {
        Integer blockedUserNo = body.get("blockedUserNo");
        boolean isNowBlocked = blockService.toggleBlock(blockedUserNo);
        return Map.of("success", true, "isBlocked", isNowBlocked);
    }

    // GET /block/check?blockedUserNo=123
    @GetMapping("/check")
    public Map<String, Object> checkBlock(@RequestParam Integer blockedUserNo) {
        boolean result = blockService.isBlocked(blockedUserNo);
        return Map.of("isBlocked", result);
    }
}
