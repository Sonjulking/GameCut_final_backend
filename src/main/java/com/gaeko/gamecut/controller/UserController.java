package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/listUser")
    public List<UserDTO> findAll() {
        return userService.findAll();
    }
    
    @PostMapping("/user/join")
    public Map<String, Object> join(@RequestBody UserDTO dto) {
    	System.out.println("user/join으로 넘어옴.");
        boolean result = userService.register(dto);
        return Map.of("success", result);
    }

    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        boolean result = userService.login(body.get("userId"), body.get("pwd"));
        return Map.of("success", result);
    }

    @PostMapping("/user/findPassword")
    public Map<String, Object> findPassword(@RequestBody Map<String, String> body) {
        boolean result = userService.findPassword(body.get("userId"), body.get("email"));
        return Map.of("success", result);
    }
}
