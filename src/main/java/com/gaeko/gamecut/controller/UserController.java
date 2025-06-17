package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.repository.UserRepository;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
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
        return userService.loginWithToken(body.get("userId"), body.get("pwd"));
    }

    @PostMapping("/user/findPassword")
    public Map<String, Object> findPassword(@RequestBody Map<String, String> body) {
        boolean result = userService.findPassword(body.get("userId"), body.get("email"));
        return Map.of("success", result);
    }


    // 아이디 중복확인
    @GetMapping("/user/checkUserId")
    public Map<String, Boolean> checkUserId(@RequestParam String userId) {
        boolean exists = userService.isUserIdExists(userId);
        return Map.of("exists", exists);
    }

    // 닉네임 중복확인
    @GetMapping("/user/checkUserNickname")
    public Map<String, Boolean> checkUserNickname(@RequestParam String userNickname) {
        boolean exists = userService.isUserNicknameExists(userNickname);
        return Map.of("exists", exists);
    }


    //유저 정보가저오기

    @GetMapping("/user/myinfo")
    public UserDTO getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();  // 현재 로그인된 유저의 아이디

        return userService.findUserByUserId(userId);
    }

    @PostMapping("/user/oauth/google")
    public Map<String, Object> googleLogin(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        return userService.googleLogin(token);
    }

    @PostMapping("/user/oauth/naver")
    public Map<String, Object> naverLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String state = body.get("state");
        return userService.naverLogin(code, state);
    }

    @GetMapping("/user/{userNo}")
    public UserDTO findUserByUserNo(@PathVariable Integer userNo) {
        System.out.println(userService.findUserByUserNo(userNo));
        return userService.findUserByUserNo(userNo);
    }
}
