package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.MessageDTO;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.MessageRepository;
import com.gaeko.gamecut.service.MessageService;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestBody MessageDTO dto, Authentication authentication) {
        try {
            User currentUser = userService.getCurrentUser();
            dto.setSendUserNo(currentUser.getUserNo()); // 보안상 서버에서 덮어씀

            messageService.sendMessage(dto);
            return Map.of("success", true, "message", "쪽지가 전송되었습니다.");
        } catch (IllegalStateException e) {
            return Map.of("success", false, "message", e.getMessage());
        } catch (Exception e) {
            return Map.of("success", false, "message", "쪽지 전송 실패: " + e.getMessage());
        }
    }

    @GetMapping("/received")
    public List<MessageDTO> getReceivedMessages() {
        User user = userService.getCurrentUser();
        return messageService.getReceivedMessages(user);
    }

    @GetMapping("/sent")
    public List<MessageDTO> getSentMessages() {
        User user = userService.getCurrentUser();
        return messageService.getSentMessages(user);
    }

    @DeleteMapping("/{messageNo}")
    public Map<String, Object> deleteMessage(@PathVariable Integer messageNo) {
        messageService.softDeleteMessage(messageNo);
        return Map.of("success", true, "message", "쪽지가 삭제되었습니다.");
    }
}
