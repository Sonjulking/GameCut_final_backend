package com.gaeko.gamecut.controller;

import com.gaeko.gamecut.dto.UserDTO;
import com.gaeko.gamecut.jwt.JwtUtil;
import com.gaeko.gamecut.repository.UserRepository;
import com.gaeko.gamecut.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

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

//    @PostMapping("/user/login")
//    public Map<String, Object> login(@RequestBody Map<String, String> body) {
//        return userService.loginWithToken(body.get("userId"), body.get("pwd"));
//    }

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
        String token = body.get("accessToken");
        return userService.googleLogin(token);
    }

    @PostMapping("/user/oauth/naver")
    public Map<String, Object> naverLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String state = body.get("state");
        return userService.naverLogin(code, state);
    }

    
    @PostMapping("/user/email/send")
    public Map<String, Object> sendEmailCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return userService.sendEmailCode(email);
    }

    @PostMapping("/user/email/verify")
    public Map<String, Object> verifyEmailCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        return userService.verifyEmailCode(email, code);
    }


    @GetMapping("/user/{userNo}")
    public UserDTO findUserByUserNo(@PathVariable Integer userNo) {
        System.out.println(userService.findUserByUserNo(userNo));
        return userService.findUserByUserNo(userNo);
    }

    @PutMapping("/user/delete/{userid}")
    public void deleteUser(@PathVariable String userid) {
        userService.userDelete(userid);
    }
    
    
    @PostMapping("/user/refresh")
    public Map<String, Object> refreshAccessToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        try {
            String userId = jwtUtil.getUserId(refreshToken);
            String stored = userService.getRefreshTokenForUser(userId);
            if (!refreshToken.equals(stored)) {
                return Map.of("success", false, "message", "유효하지 않은 토큰");
            }

            String newAccessToken = jwtUtil.createToken(userId, "USER"); // role은 필요시 DB에서 조회
            return Map.of("success", true, "token", newAccessToken);
        } catch (Exception e) {
            return Map.of("success", false, "message", "토큰 만료 또는 위조됨");
        }
    }
    
    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = userService.loginWithToken(body.get("userId"), body.get("pwd"));

        if (!(Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        }

        String refreshToken = (String) result.get("refreshToken");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .path("/")
            .maxAge(60 * 60 * 24 * 7) // 7일
            .secure(false) // 프로덕션에서는 true
            .sameSite("Lax")
            .build();

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(result);
    }

}
