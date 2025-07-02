package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.Block;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.BlockRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    // 현재 로그인 유저 가져오기
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserId(auth.getName()).orElseThrow();
    }

    // 차단 토글 기능
    @Transactional
    public boolean toggleBlock(Integer blockedUserNo) {
        User blocker = getCurrentUser();
        User blocked = userRepository.findUserByUserNo(blockedUserNo);

        boolean isBlocked = blockRepository.existsByBlockerAndBlocked(blocker, blocked);
        if (isBlocked) {
            blockRepository.deleteByBlockerAndBlocked(blocker, blocked);
        } else {
            Block block = Block.builder()
                    .blocker(blocker)
                    .blocked(blocked)
                    .build();
            blockRepository.save(block);
        }
        return !isBlocked;
    }

    // 차단 여부 확인
    public boolean isBlocked(Integer blockedUserNo) {
        User blocker = getCurrentUser();
        User blocked = userRepository.findUserByUserNo(blockedUserNo);
        return blockRepository.existsByBlockerAndBlocked(blocker, blocked);
    }
}
