package com.gaeko.gamecut.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaeko.gamecut.entity.GuessTheRank;
import com.gaeko.gamecut.service.GuessTheRankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GuessTheRankController {
  private final GuessTheRankService gameService;

  @GetMapping("/question")
  public ResponseEntity<?> getQuestion() {
    GuessTheRank q = gameService.getRandomQuestion();
    // VideoDTO 에서 파일 URL 꺼내서 전달
    Map<String,Object> body = Map.of(
      "gtrNo", q.getGtrNo(),
      "videoUrl", q.getVideo().getAttachFile().getRealPath(), 
      "options", List.of("아이언","브론즈","골드","다이아")
    );
    return ResponseEntity.ok(body);
  }

  @PostMapping("/answer")
  public ResponseEntity<?> submit(
     @RequestBody Map<String,Object> req
  ) {
    Integer gtrNo = (Integer)req.get("gtrNo");
    String selected = (String)req.get("tier");
    boolean correct = gameService.submitAnswer(gtrNo, selected);
    return ResponseEntity.ok(Map.of("correct", correct));
  }
}
