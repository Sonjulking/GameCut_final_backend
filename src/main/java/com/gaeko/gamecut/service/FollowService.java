package com.gaeko.gamecut.service;

import com.gaeko.gamecut.entity.Follow;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.repository.FollowRepository;
import com.gaeko.gamecut.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleFollow(Integer toUserNo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        User fromUser = userRepository.findByUserId(userId).orElseThrow();
        User toUser = userRepository.findUserByUserNo(toUserNo);

        boolean isFollowing = followRepository.existsByFolloweeAndFollower(toUser, fromUser);

        if (isFollowing) {
            followRepository.deleteByFolloweeAndFollower(toUser, fromUser);
        } else {
            Follow follow = Follow.builder()
                    .followee(toUser)
                    .follower(fromUser)
                    .build();
            followRepository.save(follow);
        }

        return !isFollowing;
    }

    public boolean isFollowing(Integer toUserNo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        User fromUser = userRepository.findByUserId(userId).orElseThrow();
        User toUser = userRepository.findUserByUserNo(toUserNo);
        return followRepository.existsByFolloweeAndFollower(toUser, fromUser);
    }
    
    
 // 내가 팔로우한 사람 목록
    public List<User> getFollowingUsers(User me) {
        return followRepository.findAll().stream()
                .filter(f -> f.getFollower().getUserNo().equals(me.getUserNo()))
                .map(Follow::getFollowee)
                .toList();
    }

    // 나를 팔로우한 사람 목록
    public List<User> getFollowerUsers(User me) {
        return followRepository.findAll().stream()
                .filter(f -> f.getFollowee().getUserNo().equals(me.getUserNo()))
                .map(Follow::getFollower)
                .toList();
    }

}