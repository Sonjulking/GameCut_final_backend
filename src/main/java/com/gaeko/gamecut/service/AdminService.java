package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.Item;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.ItemRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public void softDeleteUser(Integer userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        if (user.getUserDeleteDate() != null) {
            throw new RuntimeException("이미 탈퇴된 유저입니다.");
        }

        user.setUserDeleteDate(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public void adminDeleteItem(Integer itemNo, String username) {
        User admin = userRepository.findByUserId(username)
            .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        if (!"ROLE_ADMIN".equals(admin.getRole())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Item item = itemRepository.findById(itemNo)
            .orElseThrow(() -> new IllegalArgumentException("아이템 없음"));

        itemRepository.delete(item); // 실제 삭제
    }
}
