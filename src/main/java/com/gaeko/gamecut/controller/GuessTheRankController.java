// 2025년 7월 8일 수정됨 - DTO + Mapper 패턴으로 리팩터링
// 2025년 7월 8일 수정됨 - realPath를 fileUrl로 변경하여 웹 접근 가능한 URL 반환
package com.gaeko.gamecut.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gaeko.gamecut.dto.FileDTO;
import com.gaeko.gamecut.dto.GuessTheRankDTO;
import com.gaeko.gamecut.dto.VideoDTO;
import com.gaeko.gamecut.service.FileService;
import com.gaeko.gamecut.service.FileUploadService;
import com.gaeko.gamecut.service.GuessTheRankService;
import com.gaeko.gamecut.service.UserService;
import com.gaeko.gamecut.service.VideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GuessTheRankController {
  private final GuessTheRankService gameService;
  private final FileUploadService fileUploadService; 
  private final UserService userService; 
  private final FileService fileService; 
  private final VideoService videoService; 

  // 2025년 7월 8일 수정됨 - DTO 기반으로 변경
  @GetMapping("/question")
  public ResponseEntity<?> getQuestion() {
    GuessTheRankDTO q = gameService.getRandomQuestion();
    // DTO에서 파일 URL 꺼내서 전달
    Map<String,Object> body = Map.of(
      "gtrNo", q.getGtrNo(),
      "videoUrl", q.getVideo().getAttachFile().getFileUrl(), 
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
    
    // 2025-07-09 수정됨 - 내 게임 기록 저장 추가
    try {
      Integer userNo = userService.getCurrentUser().getUserNo();
      gameService.saveUserGameHistory(userNo, gtrNo, selected, correct);
    } catch (Exception e) {
      // 인증되지 않은 사용자인 경우 기록 저장 생략
      System.out.println("비로그인 사용자의 게임 기록 저장 생략");
    }
    
    return ResponseEntity.ok(Map.of("correct", correct));
  }

  // 2025-07-09 수정됨 - 내 게스더랭크 기록 조회 API 추가
  @GetMapping("/my-history")
  public ResponseEntity<List<Map<String, Object>>> getMyHistory() {
    try {
      Integer userNo = userService.getCurrentUser().getUserNo();
      List<Map<String, Object>> history = gameService.getUserGameHistory(userNo);
      return ResponseEntity.ok(history);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(List.of(Map.of("error", "로그인이 필요합니다.")));
    }
  }

  // 2025년 7월 8일 수정됨 - DTO 기반으로 변경
  @GetMapping("/all")
  public ResponseEntity<List<Map<String, Object>>> getAllQuestions(
      @RequestParam(value = "gameType", required = false) String gameType) {
    List<Map<String, Object>> list = gameService.getAllByGameType(gameType).stream()
        .map(q -> {
            Map<String, Object> m = new HashMap<>();
            m.put("gtrNo", q.getGtrNo());
            m.put("videoUrl", q.getVideo().getAttachFile().getFileUrl());
            m.put("tier", q.getTier());
            m.put("gameType", q.getGameType());
            return m;
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(list);
  }


  // 2025년 7월 8일 수정됨 - DTO 기반으로 변경
  @PostMapping(
    path     = "/create",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<List<Map<String,Object>>> createGame(
      @RequestPart("files") List<MultipartFile> files,
      @RequestParam("tiers") List<String> tiers,
      @RequestParam("gameType") String gameType
  ) throws IOException {
    if (files.size() != tiers.size()) {
      return ResponseEntity.badRequest()
        .body(List.of(Map.of("error","파일 개수와 티어 개수가 일치하지 않습니다")));
    }

    var result = IntStream.range(0, files.size())
      .mapToObj(i -> {
        try {
          FileDTO f = fileUploadService.store(files.get(i));
          // 2025년 7월 8일 수정됨 - 인증 사용자 없을 때 기본값 사용
          try {
            f.setUserNo(userService.getCurrentUser().getUserNo());
          } catch (Exception e) {
            // 인증되지 않은 사용자인 경우 기본값 사용
            f.setUserNo(1); // 기본 사용자 번호
          }
          f = fileService.save(f);

          VideoDTO v = videoService.saveGameVideo(f.getAttachNo());

          GuessTheRankDTO gtr = gameService.saveTier(v.getVideoNo(), tiers.get(i), gameType);

          return Map.<String,Object>of(
            "gtrNo",    gtr.getGtrNo(),
            "videoNo",  gtr.getVideoNo(),
            "videoUrl", v.getAttachFile().getFileUrl(),
            "tier",     gtr.getTier(),
            "gameType", gtr.getGameType()
          );
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      })
      .collect(Collectors.toList());

    return ResponseEntity.ok(result);
  }

}
