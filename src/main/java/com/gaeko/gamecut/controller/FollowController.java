package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.entity.User;
import com.gaeko.gamecut.mapper.UserMapper;
import com.gaeko.gamecut.service.FollowService;
import com.gaeko.gamecut.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public Map<String, Object> toggleFollow(@RequestBody Map<String, Integer> body) {
        Integer toUserNo = body.get("toUserNo");
        boolean isNowFollowing = followService.toggleFollow(toUserNo);
        return Map.of("success", true, "isFollowing", isNowFollowing);
    }

    @GetMapping("/check")
    public Map<String, Object> checkFollow(@RequestParam Integer toUserNo) {
        boolean result = followService.isFollowing(toUserNo);
        return Map.of("isFollowing", result);
    }
    
    
    @GetMapping("/followers")
    public List<UserDTO> getFollowers() {
        User me = userService.getCurrentUser();
        return followService.getFollowerUsers(me).stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @GetMapping("/following")
    public List<UserDTO> getFollowing() {
        User me = userService.getCurrentUser();
        return followService.getFollowingUsers(me).stream()
                .map(userMapper::toDTO)
                .toList();
    }

}
