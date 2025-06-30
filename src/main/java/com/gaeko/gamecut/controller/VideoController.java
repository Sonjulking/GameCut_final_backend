package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.service.BoardService;
import com.gaeko.gamecut.service.GuessTheRankService;
import com.gaeko.gamecut.service.VideoService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final GuessTheRankService rankService;
    private final VideoService videoService;
    private final BoardService boardService;  
    
    /** POST /api/videos/{videoNo}/tier?tier=골드 */
    @PostMapping("/{videoNo}/tier")
    public ResponseEntity<?> setTier(
        @PathVariable Integer videoNo,
        @RequestParam String tier
    ) {
        rankService.saveTier(videoNo, tier);
        return ResponseEntity.ok().build();
    }

     /** 모든 영상 조회 GET /api/videos */
    @GetMapping
    public ResponseEntity<List<VideoDTO>> fetchAll() {
        return ResponseEntity.ok(videoService.findAllVideos());
    }

    /**  
    * GET /api/videos/all  
    * 게시판에 올라온 모든 영상 게시물을 VideoDTO 형태로 돌려줍니다.  
    **/
    @GetMapping("/all")
    public List<VideoDTO> listAllVideos() {
        return boardService.findAllVideoBoards();
    }
}
