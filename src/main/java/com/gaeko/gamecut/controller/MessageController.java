package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.MessageDTO;
import com.gaeko.gamecut.entity.Message;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.MessageRepository;
import com.gaeko.gamecut.service.MessageService;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
            // 현재 로그인한 사용자 확인
            User currentUser = userService.getCurrentUser();

            // 보안: 프론트에서 보낸 발신자 번호가 조작되었을 수 있으므로 서버에서 override
            dto.setSendUserNo(currentUser.getUserNo());

            messageService.sendMessage(dto);
            return Map.of("success", true, "message", "쪽지가 전송되었습니다.");
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
    
    @GetMapping("/received")
    public List<MessageDTO> getReceivedMessages() {
        User user = userService.getCurrentUser();
        return messageService.getReceivedMessages(user); // 💡 여기서 이미 DTO로 변환된 리스트를 가져옴
    }

    
    @DeleteMapping("/{messageNo}")
    public Map<String, Object> deleteMessage(@PathVariable Integer messageNo) {
        messageService.softDeleteMessage(messageNo);
        return Map.of("success", true, "message", "쪽지가 삭제되었습니다.");
    }
    
    @GetMapping("/sent")
    public List<MessageDTO> getSentMessages() {
        User user = userService.getCurrentUser();
        return messageService.getSentMessages(user);
    }




}
