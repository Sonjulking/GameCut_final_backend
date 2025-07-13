package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.MessageDTO;
import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.repository.MessageRepository;
import com.gaeko.gamecut.service.MessageService;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper; // ✅ 추가

    // 쪽지 전송
    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestBody MessageDTO dto) {
        try {
            User currentUser = userService.getCurrentUser();
            dto.setSendUserNo(currentUser.getUserNo()); // 보안상 서버에서 설정
            messageService.sendMessage(dto);
            return Map.of("success", true, "message", "쪽지가 전송되었습니다.");
        } catch (IllegalStateException e) {
            return Map.of("success", false, "message", e.getMessage());
        } catch (Exception e) {
            return Map.of("success", false, "message", "쪽지 전송 실패: " + e.getMessage());
        }
    }

    // 받은 쪽지 목록
    @GetMapping("/received")
    public List<MessageDTO> getReceivedMessages() {
        User user = userService.getCurrentUser();
        return messageService.getReceivedMessages(user);
    }

    // 보낸 쪽지 목록
    @GetMapping("/sent")
    public List<MessageDTO> getSentMessages() {
        User user = userService.getCurrentUser();
        return messageService.getSentMessages(user);
    }

    // 쪽지 삭제 (soft delete)
    @DeleteMapping("/{messageNo}")
    public Map<String, Object> deleteMessage(@PathVariable Integer messageNo) {
        messageService.softDeleteMessage(messageNo);
        return Map.of("success", true, "message", "쪽지가 삭제되었습니다.");
    }

    // ✅ 받은 쪽지 총 개수 조회
    @GetMapping("/unread/count")
    public ResponseEntity<?> getReceivedMessageCount(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 🔧 UserDTO → User 변환
        UserDTO dto = userService.findUserByUserId(userDetails.getUsername());
        User user = userMapper.toEntity(dto);

        long count = messageService.getReceivedMessageCount(user);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
