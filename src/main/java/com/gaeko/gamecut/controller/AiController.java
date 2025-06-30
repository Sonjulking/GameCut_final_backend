package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        try {
            String userMessage = body.get("message");
            String response = aiService.askGpt(loginUser.getUsername(), userMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("AI Service 요청 실패");
        }
    }

    @PostMapping("/tag")
    public ResponseEntity<List<String>> recommendTags(
            @RequestParam String title,
            @RequestParam String content,
            @AuthenticationPrincipal UserDetails loginUser
    ) throws IOException {
        List<String> tags = aiService.generateTags(loginUser.getUsername(), title, content);
        return ResponseEntity.ok(tags);
    }

}
