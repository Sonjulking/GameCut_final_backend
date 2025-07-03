package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public void softDeleteUser(Integer userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        if (user.getUserDeleteDate() != null) {
            throw new RuntimeException("이미 탈퇴된 유저입니다.");
        }

        user.setUserDeleteDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
