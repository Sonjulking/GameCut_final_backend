package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AiChatController {
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


    //@PostMapping("/tag")


}
