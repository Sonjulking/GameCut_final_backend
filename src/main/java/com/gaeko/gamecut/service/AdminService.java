package com.gaeko.gamecut.service;

import com.gaeko.gamecut.dto.ItemDTO;
import com.gaeko.gamecut.entity.Item;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.ItemRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

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
    
    // 2025-07-16 수정됨 - 소프트 삭제 방식으로 구현
public void adminDeleteItem(Integer itemNo) {
    // Item 엔티티 조회
    Item item = itemRepository.findById(itemNo)
                              .orElseThrow(() -> new RuntimeException("해당 아이템이 존재하지 않습니다."));
    
    // 이미 삭제된 아이템인지 확인
    if (item.getItemDeleteDate() != null) {
        throw new RuntimeException("이미 삭제된 아이템입니다.");
    }
    
    // 삭제 날짜를 현재 날짜로 설정
    item.setItemDeleteDate(new Date());
    // 또는 LocalDate.now() - 필드 타입에 따라
    
    // 변경된 정보 저장
    itemRepository.save(item);
}
}
